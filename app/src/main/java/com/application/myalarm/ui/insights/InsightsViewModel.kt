package com.application.myalarm.ui.insights

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.myalarm.AlarmApplication
import com.application.myalarm.data.db.MissionHistoryEntity
import com.application.myalarm.data.repository.DayResult
import com.application.myalarm.data.repository.LifetimeStats
import com.application.myalarm.data.repository.MissionBreakdownItem
import com.application.myalarm.data.repository.MissionHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InsightsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AlarmApplication
    private val missionHistoryRepository = MissionHistoryRepository(app.database.missionHistoryDao())

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _lifetimeStats = MutableStateFlow(
        LifetimeStats(
            bestStreak = 0,
            dismisses = 0,
            successRate = 0.0f,
            avgTimeMs = 0L,
            skipped = 0,
            totalRuns = 0
        )
    )
    val lifetimeStats: StateFlow<LifetimeStats> = _lifetimeStats.asStateFlow()

    private val _weeklyHeatmap = MutableStateFlow<List<DayResult>>(emptyList())
    val weeklyHeatmap: StateFlow<List<DayResult>> = _weeklyHeatmap.asStateFlow()

    private val _missionBreakdown = MutableStateFlow<List<MissionBreakdownItem>>(emptyList())
    val missionBreakdown: StateFlow<List<MissionBreakdownItem>> = _missionBreakdown.asStateFlow()

    val recentMissions: StateFlow<List<MissionHistoryEntity>> =
        missionHistoryRepository.getRecentMissions(20)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _currentStreak.value = missionHistoryRepository.calculateCurrentStreak()
        }
        viewModelScope.launch {
            _lifetimeStats.value = missionHistoryRepository.getLifetimeStats()
        }
        viewModelScope.launch {
            _weeklyHeatmap.value = missionHistoryRepository.getWeeklyHeatmap()
        }
        viewModelScope.launch {
            _missionBreakdown.value = missionHistoryRepository.getMissionBreakdown()
        }
    }
}
