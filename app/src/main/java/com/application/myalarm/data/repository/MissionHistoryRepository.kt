package com.application.myalarm.data.repository

import com.application.myalarm.data.db.MissionHistoryDao
import com.application.myalarm.data.db.MissionHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class LifetimeStats(
    val bestStreak: Int,
    val dismisses: Int,
    val successRate: Float,
    val avgTimeMs: Long?,
    val skipped: Int,
    val totalRuns: Int
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

    suspend fun getByDateRange(start: Long, end: Long): List<MissionHistoryEntity> {
        return dao.getByDateRange(start, end)
    }

    suspend fun getLifetimeStats(): LifetimeStats {
        val totalRuns = dao.getTotalCount()
        val successCount = dao.getSuccessCount()
        val skippedCount = dao.getSkippedCount()
        val avgTime = dao.getAvgDurationMs()
        val bestStreak = calculateBestStreak()
        val successRate = if (totalRuns > 0) successCount.toFloat() / totalRuns else 0f

        return LifetimeStats(
            bestStreak = bestStreak,
            dismisses = successCount,
            successRate = successRate,
            avgTimeMs = avgTime,
            skipped = skippedCount,
            totalRuns = totalRuns
        )
    }

    suspend fun getMissionBreakdown(): List<MissionBreakdownItem> {
        val allEntries = dao.getByDateRange(0L, System.currentTimeMillis())
        return allEntries
            .groupBy { it.missionType }
            .map { (type, entries) ->
                MissionBreakdownItem(
                    missionType = type,
                    totalAttempts = entries.size,
                    successCount = entries.count { it.result == "SUCCESS" }
                )
            }
    }

    suspend fun calculateCurrentStreak(): Int {
        var streak = 0
        var currentDate = LocalDate.now()
        val zone = ZoneId.systemDefault()

        while (true) {
            val startOfDay = currentDate.atStartOfDay(zone).toInstant().toEpochMilli()
            val endOfDay = currentDate.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
            val dayEntries = dao.getByDateRange(startOfDay, endOfDay)

            val hasSuccess = dayEntries.any { it.result == "SUCCESS" }
            if (hasSuccess) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }

    suspend fun getWeeklyHeatmap(weeks: Int = 4): List<DayResult> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()
        val totalDays = weeks * 7
        val startDate = today.minusDays(totalDays.toLong() - 1)

        val startMillis = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis = today.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        val allEntries = dao.getByDateRange(startMillis, endMillis)

        val entriesByDate = allEntries.groupBy { entry ->
            java.time.Instant.ofEpochMilli(entry.completedAt)
                .atZone(zone)
                .toLocalDate()
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
        return results
    }

    private suspend fun calculateBestStreak(): Int {
        val zone = ZoneId.systemDefault()
        val allEntries = dao.getByDateRange(0L, System.currentTimeMillis())
        if (allEntries.isEmpty()) return 0

        val successDates = allEntries
            .filter { it.result == "SUCCESS" }
            .map { entry ->
                java.time.Instant.ofEpochMilli(entry.completedAt)
                    .atZone(zone)
                    .toLocalDate()
            }
            .distinct()
            .sorted()

        if (successDates.isEmpty()) return 0

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
        return bestStreak
    }
}
