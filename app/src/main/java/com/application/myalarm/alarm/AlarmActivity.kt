package com.application.myalarm.alarm

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.application.myalarm.AlarmApplication
import com.application.myalarm.data.db.AlarmEntity
import com.application.myalarm.data.db.MissionHistoryEntity
import com.application.myalarm.ui.alarm.AlarmRingingScreen
import com.application.myalarm.ui.theme.MyAlarmTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmActivity : ComponentActivity() {
    private var isDismissed = false

    companion object {
        var isRequestingPermission = false
        var isActivityVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRequestingPermission = false

        // Enable showing over lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        enableEdgeToEdge()

        // Hide system bars and configure BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

        // Extract alarm data from intent
        val alarmId = intent.getLongExtra("alarm_id", -1L)
        val missionType = intent.getStringExtra("mission_type") ?: "MATH_PROBLEM"
        val label = intent.getStringExtra("label") ?: ""
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val soundName = intent.getStringExtra("sound_name") ?: "Basic"
        val soundVolume = intent.getFloatExtra("sound_volume", 1.0f)
        val customSoundUri = intent.getStringExtra("custom_sound_uri")
        val stepCountLevel = intent.getIntExtra("step_count_level", 1)
        val scannedCodeValue = intent.getStringExtra("scanned_code_value") ?: ""
        val isBackupEnabled = intent.getBooleanExtra("is_backup_enabled", false)
        val backupMinutes = intent.getIntExtra("backup_minutes", 5)

        // Schedule backup alarm if enabled
        if (isBackupEnabled) {
            val backupAlarm = AlarmEntity(
                id = alarmId,
                hour = hour,
                minute = minute,
                label = label,
                missionType = missionType,
                soundName = soundName,
                soundVolume = soundVolume,
                customSoundUri = customSoundUri,
                stepCountLevel = stepCountLevel,
                scannedCodeValue = scannedCodeValue,
                isBackupAlarmEnabled = true,
                backupAlarmMinutes = backupMinutes
            )
            AlarmScheduler.scheduleBackup(this, backupAlarm)
        }

        setContent {
            MyAlarmTheme {
                AlarmRingingScreen(
                    alarmId = alarmId,
                    hour = hour,
                    minute = minute,
                    label = label,
                    missionType = missionType,
                    stepCountLevel = stepCountLevel,
                    scannedCodeValue = scannedCodeValue,
                    onDismiss = { durationMs ->
                        dismissAlarm(alarmId, missionType, durationMs, isBackupEnabled, hour, minute)
                    }
                )
            }
        }
    }

    private fun dismissAlarm(
        alarmId: Long,
        missionType: String,
        durationMs: Long,
        isBackupEnabled: Boolean,
        hour: Int,
        minute: Int
    ) {
        isDismissed = true
        // Stop AlarmService
        stopService(Intent(this, AlarmService::class.java))

        // Cancel backup alarm if one was scheduled
        if (isBackupEnabled) {
            AlarmScheduler.cancel(this, alarmId)
        }

        // Record mission in history and reschedule if repeating
        val app = application as AlarmApplication
        CoroutineScope(Dispatchers.IO).launch {
            // Record mission history
            val historyDao = app.database.missionHistoryDao()
            historyDao.insert(
                MissionHistoryEntity(
                    alarmId = alarmId,
                    missionType = missionType,
                    result = "SUCCESS",
                    durationMs = durationMs
                )
            )

            // Reschedule alarm for next occurrence if repeating
            val alarmDao = app.database.alarmDao()
            val alarm = alarmDao.getById(alarmId)
            if (alarm != null) {
                if (alarm.repeatDays != 0) {
                    // Repeating alarm: reschedule for next occurrence
                    AlarmScheduler.schedule(this@AlarmActivity, alarm)
                } else {
                    // One-shot alarm: disable it
                    alarmDao.update(alarm.copy(isEnabled = false))
                }
            }
        }

        finish()
    }

    @Deprecated("Use the OnBackPressedDispatcher instead.")
    override fun onBackPressed() {
        // Prevent dismissal via back button – user must complete the mission
    }

    override fun onStart() {
        super.onStart()
        isActivityVisible = true
    }

    override fun onResume() {
        super.onResume()
        isRequestingPermission = false
    }

    override fun onPause() {
        super.onPause()
        if (shouldRelaunch()) {
            relaunchActivity()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (shouldRelaunch()) {
            relaunchActivity()
        }
    }

    override fun onStop() {
        super.onStop()
        isActivityVisible = false
        if (shouldRelaunch()) {
            relaunchActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityVisible = false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && shouldRelaunch()) {
            collapseNotifications()
            relaunchActivity()
        }
    }

    private fun shouldRelaunch(): Boolean {
        if (isDismissed || isRequestingPermission) return false
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        val isInteractive = powerManager.isInteractive
        val isLocked = keyguardManager?.isKeyguardLocked ?: false
        return isInteractive && !isLocked
    }

    private fun relaunchActivity() {
        try {
            val intent = Intent(this, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun collapseNotifications() {
        try {
            @Suppress("WrongConstant")
            val statusBarService = getSystemService("statusbar")
            val statusBarManagerClass = Class.forName("android.app.StatusBarManager")
            val collapseMethod = statusBarManagerClass.getMethod("collapsePanels")
            collapseMethod.invoke(statusBarService)
        } catch (e: Exception) {
            // Ignore
        }
    }
}
