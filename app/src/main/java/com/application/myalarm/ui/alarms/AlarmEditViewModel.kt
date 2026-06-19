package com.application.myalarm.ui.alarms

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.myalarm.AlarmApplication
import com.application.myalarm.alarm.AlarmScheduler
import com.application.myalarm.data.db.AlarmEntity
import com.application.myalarm.data.repository.AlarmRepository
import com.application.myalarm.mission.MissionType
import kotlinx.coroutines.launch
import java.time.Instant

class AlarmEditViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AlarmApplication
    private val alarmRepository = AlarmRepository(app.database.alarmDao())

    var alarmId by mutableLongStateOf(-1L)
        private set
    var hour by mutableIntStateOf(8)
    var minute by mutableIntStateOf(0)
    var label by mutableStateOf("")
    var repeatDays by mutableIntStateOf(0b0011111) // Mon-Fri default
    var missionType by mutableStateOf(MissionType.MATH_PROBLEM.name)
    var soundName by mutableStateOf("Basic")
    var soundVolume by mutableFloatStateOf(1.0f)
    var customSoundUri by mutableStateOf<String?>(null)
    var stepCountLevel by mutableIntStateOf(1)
    var scannedCodeValue by mutableStateOf("")
    var isBackupAlarmEnabled by mutableStateOf(false)
    var backupAlarmMinutes by mutableIntStateOf(5)

    val isEditing: Boolean
        get() = alarmId > 0L

    var isSaving by mutableStateOf(false)
        private set

    fun loadAlarm(id: Long) {
        if (alarmId == id) return

        if (id <= 0L) {
            alarmId = -1L
            hour = 8
            minute = 0
            label = ""
            repeatDays = 0b0011111 // Mon-Fri default
            missionType = MissionType.MATH_PROBLEM.name
            soundName = "Basic"
            soundVolume = 1.0f
            customSoundUri = null
            stepCountLevel = 1
            scannedCodeValue = ""
            isBackupAlarmEnabled = false
            backupAlarmMinutes = 5
            return
        }

        alarmId = id
        viewModelScope.launch {
            val alarm = alarmRepository.getById(id)
            if (alarm != null && alarmId == id) {
                hour = alarm.hour
                minute = alarm.minute
                label = alarm.label
                repeatDays = alarm.repeatDays
                missionType = alarm.missionType
                soundName = alarm.soundName
                soundVolume = alarm.soundVolume
                customSoundUri = alarm.customSoundUri
                stepCountLevel = alarm.stepCountLevel
                scannedCodeValue = alarm.scannedCodeValue
                isBackupAlarmEnabled = alarm.isBackupAlarmEnabled
                backupAlarmMinutes = alarm.backupAlarmMinutes
            }
        }
    }

    fun toggleDay(dayBit: Int) {
        repeatDays = repeatDays xor (1 shl dayBit)
    }

    fun isDaySelected(dayBit: Int): Boolean {
        return (repeatDays and (1 shl dayBit)) != 0
    }

    fun incrementBackupMinutes() {
        if (backupAlarmMinutes < 30) backupAlarmMinutes++
    }

    fun decrementBackupMinutes() {
        if (backupAlarmMinutes > 1) backupAlarmMinutes--
    }

    fun saveAlarm(onComplete: () -> Unit) {
        if (isSaving) return
        isSaving = true
        viewModelScope.launch {
            val alarm = AlarmEntity(
                id = if (isEditing) alarmId else 0L,
                hour = hour,
                minute = minute,
                label = label,
                isEnabled = true,
                repeatDays = repeatDays,
                missionType = missionType,
                soundName = soundName,
                soundVolume = soundVolume,
                customSoundUri = customSoundUri,
                stepCountLevel = stepCountLevel,
                scannedCodeValue = scannedCodeValue,
                isBackupAlarmEnabled = isBackupAlarmEnabled,
                backupAlarmMinutes = backupAlarmMinutes,
                createdAt = if (isEditing) {
                    alarmRepository.getById(alarmId)?.createdAt ?: Instant.now().toEpochMilli()
                } else {
                    Instant.now().toEpochMilli()
                }
            )
            if (isEditing) {
                alarmRepository.updateAlarm(alarm)
            } else {
                alarmRepository.insertAlarm(alarm)
            }
            AlarmScheduler.schedule(getApplication<AlarmApplication>(), alarm)
            isSaving = false
            onComplete()
        }
    }
}
