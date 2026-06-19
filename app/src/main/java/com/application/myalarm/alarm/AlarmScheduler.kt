package com.application.myalarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.application.myalarm.AlarmApplication
import com.application.myalarm.data.db.AlarmEntity
import kotlinx.coroutines.runBlocking
import java.util.Calendar

object AlarmScheduler {

    private const val BACKUP_REQUEST_CODE_OFFSET = 10_000

    fun schedule(context: Context, alarm: AlarmEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val triggerTime = calculateNextTriggerTime(alarm.hour, alarm.minute, alarm.repeatDays)

        val intent = createAlarmIntent(context, alarm)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val showIntent = createShowIntent(context, alarm)
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTime, showIntent)

        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    fun cancel(context: Context, alarmId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.application.myalarm.ALARM_TRIGGER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()

        // Also cancel any backup alarm
        val backupIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.application.myalarm.BACKUP_ALARM_TRIGGER"
        }
        val backupPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt() + BACKUP_REQUEST_CODE_OFFSET,
            backupIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(backupPendingIntent)
        backupPendingIntent.cancel()
    }

    fun scheduleBackup(context: Context, alarm: AlarmEntity) {
        if (!alarm.isBackupAlarmEnabled) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val triggerTime = System.currentTimeMillis() + alarm.backupAlarmMinutes * 60 * 1000L

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.application.myalarm.BACKUP_ALARM_TRIGGER"
            putExtra("alarm_id", alarm.id)
            putExtra("hour", alarm.hour)
            putExtra("minute", alarm.minute)
            putExtra("label", alarm.label)
            putExtra("mission_type", alarm.missionType)
            putExtra("sound_name", alarm.soundName)
            putExtra("sound_volume", alarm.soundVolume)
            putExtra("custom_sound_uri", alarm.customSoundUri)
            putExtra("step_count_level", alarm.stepCountLevel)
            putExtra("scanned_code_value", alarm.scannedCodeValue)
            putExtra("is_backup_enabled", false) // Don't chain backup alarms
            putExtra("backup_minutes", alarm.backupAlarmMinutes)
            putExtra("is_backup_alarm", true)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt() + BACKUP_REQUEST_CODE_OFFSET,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val showIntent = createShowIntent(context, alarm)
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTime, showIntent)

        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    fun rescheduleAll(context: Context) {
        val app = context.applicationContext as AlarmApplication
        val alarmDao = app.database.alarmDao()

        runBlocking {
            val enabledAlarms = alarmDao.getEnabled()
            for (alarm in enabledAlarms) {
                schedule(context, alarm)
            }
        }
    }

    fun calculateNextTriggerTime(hour: Int, minute: Int, repeatDays: Int): Long {
        val now = Calendar.getInstance()

        if (repeatDays == 0) {
            // One-shot alarm: next occurrence today if in the future, else tomorrow
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (target.timeInMillis <= now.timeInMillis) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis
        }

        // Repeating alarm: find the next matching weekday
        // Bitmask: bit0 = Monday, bit1 = Tuesday, ... bit6 = Sunday
        // Calendar: SUNDAY=1, MONDAY=2, TUESDAY=3, ..., SATURDAY=7
        val calendarDayToMaskBit = mapOf(
            Calendar.MONDAY to 0,
            Calendar.TUESDAY to 1,
            Calendar.WEDNESDAY to 2,
            Calendar.THURSDAY to 3,
            Calendar.FRIDAY to 4,
            Calendar.SATURDAY to 5,
            Calendar.SUNDAY to 6
        )

        // Check up to 7 days from now
        for (daysAhead in 0..6) {
            val candidate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, daysAhead)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Skip if candidate is in the past (for today)
            if (candidate.timeInMillis <= now.timeInMillis) continue

            val dayOfWeek = candidate.get(Calendar.DAY_OF_WEEK)
            val bitIndex = calendarDayToMaskBit[dayOfWeek] ?: continue

            if (repeatDays and (1 shl bitIndex) != 0) {
                return candidate.timeInMillis
            }
        }

        // Fallback: should not reach here if repeatDays has at least one bit set
        // Schedule for next week's first matching day
        val candidate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 7)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return candidate.timeInMillis
    }

    private fun createAlarmIntent(context: Context, alarm: AlarmEntity): Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            action = "com.application.myalarm.ALARM_TRIGGER"
            putExtra("alarm_id", alarm.id)
            putExtra("hour", alarm.hour)
            putExtra("minute", alarm.minute)
            putExtra("label", alarm.label)
            putExtra("mission_type", alarm.missionType)
            putExtra("sound_name", alarm.soundName)
            putExtra("sound_volume", alarm.soundVolume)
            putExtra("custom_sound_uri", alarm.customSoundUri)
            putExtra("step_count_level", alarm.stepCountLevel)
            putExtra("scanned_code_value", alarm.scannedCodeValue)
            putExtra("is_backup_enabled", alarm.isBackupAlarmEnabled)
            putExtra("backup_minutes", alarm.backupAlarmMinutes)
            putExtra("is_backup_alarm", false)
        }
    }

    private fun createShowIntent(context: Context, alarm: AlarmEntity): PendingIntent {
        val intent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("alarm_id", alarm.id)
            putExtra("hour", alarm.hour)
            putExtra("minute", alarm.minute)
            putExtra("label", alarm.label)
            putExtra("mission_type", alarm.missionType)
            putExtra("sound_name", alarm.soundName)
            putExtra("sound_volume", alarm.soundVolume)
            putExtra("custom_sound_uri", alarm.customSoundUri)
            putExtra("step_count_level", alarm.stepCountLevel)
            putExtra("scanned_code_value", alarm.scannedCodeValue)
            putExtra("is_backup_enabled", alarm.isBackupAlarmEnabled)
            putExtra("backup_minutes", alarm.backupAlarmMinutes)
        }
        return PendingIntent.getActivity(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
