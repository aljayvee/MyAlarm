package com.application.myalarm.alarm

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("alarm_id", -1L)
        if (alarmId == -1L) return

        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val label = intent.getStringExtra("label") ?: ""
        val missionType = intent.getStringExtra("mission_type") ?: "MATH_PROBLEM"
        val soundName = intent.getStringExtra("sound_name") ?: "Basic"
        val soundVolume = intent.getFloatExtra("sound_volume", 1.0f)
        val customSoundUri = intent.getStringExtra("custom_sound_uri")
        val stepCountLevel = intent.getIntExtra("step_count_level", 1)
        val scannedCodeValue = intent.getStringExtra("scanned_code_value") ?: ""
        val isBackupEnabled = intent.getBooleanExtra("is_backup_enabled", false)
        val backupMinutes = intent.getIntExtra("backup_minutes", 5)
        val isBackupAlarm = intent.getBooleanExtra("is_backup_alarm", false)

        // Start AlarmService as a foreground service
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("alarm_id", alarmId)
            putExtra("hour", hour)
            putExtra("minute", minute)
            putExtra("label", label)
            putExtra("mission_type", missionType)
            putExtra("sound_name", soundName)
            putExtra("sound_volume", soundVolume)
            putExtra("custom_sound_uri", customSoundUri)
            putExtra("step_count_level", stepCountLevel)
            putExtra("scanned_code_value", scannedCodeValue)
            putExtra("is_backup_enabled", isBackupEnabled)
            putExtra("backup_minutes", backupMinutes)
            putExtra("is_backup_alarm", isBackupAlarm)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }

        // Launch AlarmActivity if device is unlocked (not showing keyguard)
        // If locked, rely solely on AlarmService's fullScreenIntent to launch AlarmActivity
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        val isLocked = keyguardManager?.isKeyguardLocked ?: false

        if (!isLocked) {
            val activityIntent = Intent(context, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("alarm_id", alarmId)
                putExtra("hour", hour)
                putExtra("minute", minute)
                putExtra("label", label)
                putExtra("mission_type", missionType)
                putExtra("sound_name", soundName)
                putExtra("sound_volume", soundVolume)
                putExtra("custom_sound_uri", customSoundUri)
                putExtra("step_count_level", stepCountLevel)
                putExtra("scanned_code_value", scannedCodeValue)
                putExtra("is_backup_enabled", isBackupEnabled)
                putExtra("backup_minutes", backupMinutes)
                putExtra("is_backup_alarm", isBackupAlarm)
            }
            try {
                context.startActivity(activityIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
