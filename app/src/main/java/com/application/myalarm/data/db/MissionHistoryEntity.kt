package com.application.myalarm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mission_history")
data class MissionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alarmId: Long,
    val missionType: String,
    val result: String,
    val completedAt: Long = System.currentTimeMillis(),
    val durationMs: Long = 0
)
