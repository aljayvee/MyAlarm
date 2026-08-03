package com.application.myalarm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionHistoryDao {
    @Query("SELECT * FROM mission_history ORDER BY completedAt DESC")
    fun getAll(): Flow<List<MissionHistoryEntity>>

    @Query("SELECT * FROM mission_history WHERE completedAt BETWEEN :start AND :end ORDER BY completedAt DESC")
    suspend fun getByDateRange(start: Long, end: Long): List<MissionHistoryEntity>

    @Query("SELECT * FROM mission_history ORDER BY completedAt DESC LIMIT :limit")
    fun getRecentMissions(limit: Int): Flow<List<MissionHistoryEntity>>

    @Query("SELECT * FROM mission_history WHERE completedAt >= :startOfDay ORDER BY completedAt DESC")
    fun getTodayMissions(startOfDay: Long): Flow<List<MissionHistoryEntity>>

    @Insert
    suspend fun insert(entry: MissionHistoryEntity): Long

    @Query("SELECT COUNT(*) FROM mission_history WHERE result = 'SUCCESS'")
    suspend fun getSuccessCount(): Int

    @Query("SELECT COUNT(*) FROM mission_history WHERE result = 'SKIPPED'")
    suspend fun getSkippedCount(): Int

    @Query("SELECT COUNT(*) FROM mission_history WHERE result = 'SNOOZED' OR result = 'SNOOZE'")
    suspend fun getSnoozedCount(): Int

    @Query("SELECT COUNT(*) FROM mission_history WHERE (result = 'SNOOZED' OR result = 'SNOOZE') AND completedAt BETWEEN :start AND :end")
    suspend fun getSnoozedCountInRange(start: Long, end: Long): Int

    @Query("SELECT COUNT(*) FROM mission_history WHERE result = 'DISMISSED'")
    suspend fun getDismissedCount(): Int

    @Query("SELECT MIN(completedAt) FROM mission_history")
    suspend fun getOldestCompletedAt(): Long?

    @Query("SELECT COUNT(*) FROM mission_history")
    suspend fun getTotalCount(): Int

    @Query("SELECT AVG(durationMs) FROM mission_history WHERE (result = 'SUCCESS' OR result = 'DISMISSED') AND durationMs > 0")
    suspend fun getAvgDurationMs(): Long?
}
