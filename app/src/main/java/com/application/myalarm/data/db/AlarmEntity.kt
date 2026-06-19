package com.application.myalarm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "",
    val isEnabled: Boolean = true,
    val repeatDays: Int = 0,
    val missionType: String = "MATH_PROBLEM",
    val soundName: String = "Basic",
    val soundVolume: Float = 1.0f,
    val customSoundUri: String? = null,
    val stepCountLevel: Int = 1,
    val scannedCodeValue: String = "",
    val isBackupAlarmEnabled: Boolean = false,
    val backupAlarmMinutes: Int = 5,
    val createdAt: Long = System.currentTimeMillis()
)
