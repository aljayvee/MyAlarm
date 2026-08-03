package com.application.myalarm.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.myalarm.AlarmApplication
import com.application.myalarm.data.db.AlarmEntity
import com.application.myalarm.data.db.MissionHistoryEntity
import com.application.myalarm.data.repository.AlarmRepository
import com.application.myalarm.data.repository.DayResult
import com.application.myalarm.data.repository.MissionHistoryRepository
import com.application.myalarm.data.preferences.UserPreferences
import com.application.myalarm.util.DateTimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AlarmApplication
    private val alarmRepository = AlarmRepository(app.database.alarmDao())
    private val missionHistoryRepository = MissionHistoryRepository(app.database.missionHistoryDao())
    private val userPreferences = app.userPreferences

    private val _nextAlarm = MutableStateFlow<AlarmEntity?>(null)
    val nextAlarm: StateFlow<AlarmEntity?> = _nextAlarm.asStateFlow()

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _weeklyCalendar = MutableStateFlow<List<DayResult>>(emptyList())
    val weeklyCalendar: StateFlow<List<DayResult>> = _weeklyCalendar.asStateFlow()

    private val _timeUntilAlarm = MutableStateFlow("")
    val timeUntilAlarm: StateFlow<String> = _timeUntilAlarm.asStateFlow()

    val todayMissions: StateFlow<List<MissionHistoryEntity>> =
        missionHistoryRepository.getTodayMissions()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
        startTimeUpdater()
    }

    private fun loadData() {
        viewModelScope.launch {
            alarmRepository.alarms.collect { allAlarms ->
                val enabledAlarms = allAlarms.filter { it.isEnabled }
                val now = LocalDateTime.now()
                val nearest = enabledAlarms.minByOrNull { alarm ->
                    val nextTime = DateTimeUtils.calculateNextAlarmTime(alarm.hour, alarm.minute, alarm.repeatDays)
                    java.time.Duration.between(now, nextTime).toMillis()
                }
                _nextAlarm.value = nearest
                updateTimeUntilAlarm()
            }
        }
        viewModelScope.launch {
            val streak = missionHistoryRepository.calculateCurrentStreak()
            _currentStreak.value = streak
        }
        viewModelScope.launch {
            val heatmap = missionHistoryRepository.getWeeklyHeatmap()
            val weekDates = DateTimeUtils.getWeekDates()
            val results = weekDates.map { date ->
                heatmap.find { it.date == date }
                    ?: DayResult(date, com.application.myalarm.data.repository.DayResultType.NO_RUN)
            }
            _weeklyCalendar.value = results
        }
    }

    private fun startTimeUpdater() {
        viewModelScope.launch {
            while (isActive) {
                updateTimeUntilAlarm()
                delay(60_000L)
            }
        }
    }

    private fun updateTimeUntilAlarm() {
        val alarm = _nextAlarm.value
        if (alarm != null) {
            val nextTime = DateTimeUtils.calculateNextAlarmTime(alarm.hour, alarm.minute, alarm.repeatDays)
            _timeUntilAlarm.value = DateTimeUtils.formatTimeUntil(nextTime)
        } else {
            _timeUntilAlarm.value = ""
        }
    }

    fun toggleAlarm(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            alarmRepository.toggleAlarm(id, enabled)
        }
    }

    fun refreshData() {
        loadData()
    }
}
