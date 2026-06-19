package com.application.myalarm.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    companion object {
        const val CHANNEL_ID = "alarm_channel"
        const val NOTIFICATION_ID = 1001
    }

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var userPresentReceiver: BroadcastReceiver? = null
    private var pendingAlarmIntent: Intent? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmId = intent?.getLongExtra("alarm_id", -1L) ?: -1L
        val hour = intent?.getIntExtra("hour", 0) ?: 0
        val minute = intent?.getIntExtra("minute", 0) ?: 0
        val label = intent?.getStringExtra("label") ?: ""
        val missionType = intent?.getStringExtra("mission_type") ?: "MATH_PROBLEM"
        val soundName = intent?.getStringExtra("sound_name") ?: "Basic"
        val soundVolume = intent?.getFloatExtra("sound_volume", 1.0f) ?: 1.0f
        val customSoundUri = intent?.getStringExtra("custom_sound_uri")
        val isBackupEnabled = intent?.getBooleanExtra("is_backup_enabled", false) ?: false
        val backupMinutes = intent?.getIntExtra("backup_minutes", 5) ?: 5

        createNotificationChannel()

        val notification = buildNotification(intent)
        startForeground(NOTIFICATION_ID, notification)

        startAlarmSound(soundName, soundVolume, customSoundUri)
        startVibration()

        if (intent != null) {
            registerUserPresentReceiver(intent)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarmSound()
        stopVibration()
        unregisterUserPresentReceiver()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for alarm notifications"
                setBypassDnd(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setSound(null, null) // Sound is handled by MediaPlayer
                enableVibration(false) // Vibration is handled manually
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(intent: Intent?): Notification {
        val alarmId = intent?.getLongExtra("alarm_id", -1L) ?: -1L
        val hour = intent?.getIntExtra("hour", 0) ?: 0
        val minute = intent?.getIntExtra("minute", 0) ?: 0
        val label = intent?.getStringExtra("label") ?: ""

        val formattedTime = formatTime(hour, minute)
        val contentTitle = if (label.isNotBlank()) "⏰ $label" else "⏰ Alarm Ringing"
        val contentText = "Alarm at $formattedTime – Complete mission to dismiss"

        // Full-screen intent to AlarmActivity
        val fullScreenIntent = Intent(this, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (intent != null) {
                putExtras(intent)
            }
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            alarmId.toInt(),
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()
    }

    private fun startAlarmSound(soundName: String, soundVolume: Float, customSoundUri: String?) {
        try {
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0)

            val alarmUri = if (soundName == "Custom Song" && !customSoundUri.isNullOrEmpty()) {
                Uri.parse(customSoundUri)
            } else {
                getSoundUri(this, soundName)
            }

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@AlarmService, alarmUri)
                setAudioAttributes(audioAttributes)
                isLooping = true
                prepare()
                setVolume(soundVolume, soundVolume)
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSoundUri(context: Context, soundName: String): Uri {
        val packageName = context.packageName
        val resName = when (soundName) {
            "Basic" -> "basic_alarm"
            "Alarm Clock" -> "alarm_clock"
            "Bugle Tune" -> "bugle_tune"
            "Medium Bell" -> "medium_bell_ringing_near"
            "Short Beeps" -> "beep_short"
            "Clown Horn" -> "clown_horn"
            "Wake Up" -> "wake_up_alarm"
            "Nature" -> "nature_alarm"
            "Digital Watch" -> "digital_watch_alarm"
            "Spaceship" -> "spaceship_alarm"
            "Dosimeter" -> "dosimeter_alarm"
            "Phone Alerts" -> "phone_alerts_and_rings"
            "Computer Sounds" -> "assorted_computer_sounds"
            "Alien Beam" -> "alien_beam"
            "Electric Ring" -> "electric_ring_long"
            "High Low Sweep" -> "high_to_low_sweep"
            else -> "basic_alarm"
        }
        val resId = context.resources.getIdentifier(resName, "raw", packageName)
        if (resId != 0) {
            return Uri.parse("android.resource://$packageName/$resId")
        }

        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    }


    private fun stopAlarmSound() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer = null
    }

    private fun startVibration() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val pattern = longArrayOf(0, 500, 200, 500)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createWaveform(pattern, 0) // repeat from index 0
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, 0)
        }
    }

    private fun stopVibration() {
        vibrator?.cancel()
        vibrator = null
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return "%d:%02d %s".format(displayHour, minute, amPm)
    }

    private fun launchAlarmActivity(context: Context, launchIntent: Intent) {
        val activityIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtras(launchIntent)
        }
        try {
            context.startActivity(activityIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun registerUserPresentReceiver(launchIntent: Intent) {
        pendingAlarmIntent = launchIntent
        if (userPresentReceiver == null) {
            userPresentReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == Intent.ACTION_USER_PRESENT) {
                        pendingAlarmIntent?.let { alarmIntent ->
                            if (!AlarmActivity.isActivityVisible) {
                                launchAlarmActivity(context, alarmIntent)
                            }
                        }
                    }
                }
            }
            val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
            registerReceiver(userPresentReceiver, filter)
        }
    }

    private fun unregisterUserPresentReceiver() {
        userPresentReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            userPresentReceiver = null
        }
        pendingAlarmIntent = null
    }
}
