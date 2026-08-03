package com.application.myalarm.data.repository

import com.application.myalarm.data.db.MissionHistoryDao
import com.application.myalarm.data.db.MissionHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class LifetimeStats(
    val bestStreak: Int,
    val dismisses: Int,
    val successRate: Float,
    val avgTimeMs: Long?,
    val skipped: Int,
    val totalRuns: Int,
    val totalSnoozed: Int,
    val avgSnoozePerDay: Float,
    val avgSnoozePerWeek: Float,
    val avgSnoozePerMonth: Float
)

data class SnoozeRangeStats(
    val startMs: Long,
    val endMs: Long,
    val totalSnoozed: Int,
    val daysInRange: Int,
    val avgSnoozePerDay: Float
)

data class MissionBreakdownItem(
    val missionType: String,
    val totalAttempts: Int,
    val successCount: Int
)

enum class DayResultType { SUCCESS, SKIPPED, NO_RUN }

data class DayResult(
    val date: LocalDate,
    val result: DayResultType
)

class MissionHistoryRepository(private val dao: MissionHistoryDao) {

    val allHistory: Flow<List<MissionHistoryEntity>> = dao.getAll()

    fun getRecentMissions(limit: Int): Flow<List<MissionHistoryEntity>> {
        return dao.getRecentMissions(limit)
    }

    fun getTodayMissions(): Flow<List<MissionHistoryEntity>> {
        val startOfToday = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return dao.getTodayMissions(startOfToday)
    }

    suspend fun recordMission(
        alarmId: Long,
        missionType: String,
        result: String,
        durationMs: Long
    ): Long {
        val entry = MissionHistoryEntity(
            alarmId = alarmId,
            missionType = missionType,
            result = result,
            completedAt = System.currentTimeMillis(),
            durationMs = durationMs
        )
        return dao.insert(entry)
    }

    suspend fun getByDateRange(start: Long, end: Long): List<MissionHistoryEntity> = withContext(Dispatchers.IO) {
        dao.getByDateRange(start, end)
    }

    suspend fun getLifetimeStats(): LifetimeStats = withContext(Dispatchers.IO) {
        val totalRuns = dao.getTotalCount()
        val successCount = dao.getSuccessCount()
        val skippedCount = dao.getSkippedCount()
        val dismissedCount = dao.getDismissedCount()
        val totalDismissed = successCount + skippedCount + dismissedCount

        val snoozedCount = dao.getSnoozedCount()
        val avgTime = dao.getAvgDurationMs()
        val oldestCompletedAt = dao.getOldestCompletedAt() ?: System.currentTimeMillis()

        withContext(Dispatchers.Default) {
            val bestStreak = calculateBestStreakInternal()
            val successRate = if (totalRuns > 0) totalDismissed.toFloat() / totalRuns else 0f
            val daysActive = ((System.currentTimeMillis() - oldestCompletedAt) / (1000L * 60 * 60 * 24)).coerceAtLeast(1L).toFloat()

            val avgSnoozePerDay = snoozedCount.toFloat() / daysActive
            val avgSnoozePerWeek = avgSnoozePerDay * 7f
            val avgSnoozePerMonth = avgSnoozePerDay * 30f

            LifetimeStats(
                bestStreak = bestStreak,
                dismisses = totalDismissed,
                successRate = successRate,
                avgTimeMs = avgTime,
                skipped = totalDismissed,
                totalRuns = totalRuns,
                totalSnoozed = snoozedCount,
                avgSnoozePerDay = avgSnoozePerDay,
                avgSnoozePerWeek = avgSnoozePerWeek,
                avgSnoozePerMonth = avgSnoozePerMonth
            )
        }
    }

    suspend fun getSnoozeStatsInRange(startMs: Long, endMs: Long): SnoozeRangeStats = withContext(Dispatchers.IO) {
        val totalSnoozed = dao.getSnoozedCountInRange(startMs, endMs)
        withContext(Dispatchers.Default) {
            val durationMs = (endMs - startMs).coerceAtLeast(1L)
            val days = (durationMs.toFloat() / (1000f * 60f * 60f * 24f)).coerceAtLeast(1f)
            val avgSnoozePerDay = totalSnoozed.toFloat() / days
            SnoozeRangeStats(
                startMs = startMs,
                endMs = endMs,
                totalSnoozed = totalSnoozed,
                daysInRange = days.toInt().coerceAtLeast(1),
                avgSnoozePerDay = avgSnoozePerDay
            )
        }
    }

    suspend fun getMissionBreakdown(): List<MissionBreakdownItem> = withContext(Dispatchers.IO) {
        val allEntries = dao.getByDateRange(0L, System.currentTimeMillis())
        withContext(Dispatchers.Default) {
            allEntries
                .groupBy { it.missionType }
                .map { (type, entries) ->
                    MissionBreakdownItem(
                        missionType = type,
                        totalAttempts = entries.size,
                        successCount = entries.count { it.result == "SUCCESS" }
                    )
                }
        }
    }

    suspend fun calculateCurrentStreak(): Int = withContext(Dispatchers.IO) {
        val allEntries = dao.getByDateRange(0L, System.currentTimeMillis())
        withContext(Dispatchers.Default) {
            var streak = 0
            var currentDate = LocalDate.now()
            val zone = ZoneId.systemDefault()
            val entriesByDate = allEntries.groupBy { entry ->
                safeToLocalDate(entry.completedAt, zone)
            }

            while (true) {
                val dayEntries = entriesByDate[currentDate] ?: emptyList()
                val hasSuccess = dayEntries.any { it.result == "SUCCESS" }
                if (hasSuccess) {
                    streak++
                    currentDate = currentDate.minusDays(1)
                } else {
                    break
                }
            }
            streak
        }
    }

    private fun safeToLocalDate(timestampMs: Long, zone: ZoneId): LocalDate? {
        if (timestampMs <= 0L) return null
        return try {
            java.time.Instant.ofEpochMilli(timestampMs).atZone(zone).toLocalDate()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWeeklyHeatmap(weeks: Int = 4): List<DayResult> = withContext(Dispatchers.IO) {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()
        val totalDays = weeks * 7
        val startDate = today.minusDays(totalDays.toLong() - 1)

        val startMillis = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis = today.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        val allEntries = dao.getByDateRange(startMillis, endMillis)

        withContext(Dispatchers.Default) {
            val entriesByDate = allEntries.groupBy { entry ->
                safeToLocalDate(entry.completedAt, zone)
            }

            val results = mutableListOf<DayResult>()
            var date = startDate
            while (!date.isAfter(today)) {
                val dayEntries = entriesByDate[date] ?: emptyList()
                val result = when {
                    dayEntries.any { it.result == "SUCCESS" } -> DayResultType.SUCCESS
                    dayEntries.any { it.result == "SKIPPED" } -> DayResultType.SKIPPED
                    else -> DayResultType.NO_RUN
                }
                results.add(DayResult(date = date, result = result))
                date = date.plusDays(1)
            }
            results
        }
    }

    private suspend fun calculateBestStreakInternal(): Int = withContext(Dispatchers.IO) {
        val zone = ZoneId.systemDefault()
        val allEntries = dao.getByDateRange(0L, System.currentTimeMillis())
        if (allEntries.isEmpty()) return@withContext 0

        withContext(Dispatchers.Default) {
            val successDates = allEntries
                .filter { it.result == "SUCCESS" }
                .mapNotNull { entry -> safeToLocalDate(entry.completedAt, zone) }
                .distinct()
                .sorted()

            if (successDates.isEmpty()) return@withContext 0

            var bestStreak = 1
            var currentStreak = 1

            for (i in 1 until successDates.size) {
                if (successDates[i] == successDates[i - 1].plusDays(1)) {
                    currentStreak++
                    if (currentStreak > bestStreak) {
                        bestStreak = currentStreak
                    }
                } else {
                    currentStreak = 1
                }
            }
            bestStreak
        }
    }
}
