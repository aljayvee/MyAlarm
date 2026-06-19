package com.application.myalarm.ui.alarms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.myalarm.AlarmApplication
import com.application.myalarm.alarm.AlarmScheduler
import com.application.myalarm.data.db.AlarmEntity
import com.application.myalarm.data.repository.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AlarmApplication
    private val alarmRepository = AlarmRepository(app.database.alarmDao())

    val alarms: StateFlow<List<AlarmEntity>> =
        alarmRepository.alarms
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alarmCount: StateFlow<Int> =
        alarmRepository.alarmCount
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun toggleAlarm(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            alarmRepository.toggleAlarm(id, enabled)
            val alarm = alarmRepository.getById(id)
            if (alarm != null) {
                if (enabled) {
                    AlarmScheduler.schedule(getApplication<AlarmApplication>(), alarm.copy(isEnabled = true))
                } else {
                    AlarmScheduler.cancel(getApplication<AlarmApplication>(), id)
                }
            }
        }
    }

    fun deleteAlarm(id: Long) {
        viewModelScope.launch {
            AlarmScheduler.cancel(getApplication<AlarmApplication>(), id)
            alarmRepository.deleteAlarmById(id)
        }
    }
}
