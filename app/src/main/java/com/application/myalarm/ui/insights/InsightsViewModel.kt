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
import com.application.myalarm.data.repository.SnoozeRangeStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InsightsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AlarmApplication
    private val missionHistoryRepository = MissionHistoryRepository(app.database.missionHistoryDao())

    // Independent Card 1: Current Streak
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    // Independent Card 2: Lifetime Stats (Best Streak, Total Dismissed, Success Rate, Avg Solve time, Total Runs, Total Snoozed)
    private val _lifetimeStats = MutableStateFlow(
        LifetimeStats(
            bestStreak = 0,
            dismisses = 0,
            successRate = 0.0f,
            avgTimeMs = 0L,
            skipped = 0,
            totalRuns = 0,
            totalSnoozed = 0,
            avgSnoozePerDay = 0f,
            avgSnoozePerWeek = 0f,
            avgSnoozePerMonth = 0f
        )
    )
    val lifetimeStats: StateFlow<LifetimeStats> = _lifetimeStats.asStateFlow()

    // Independent Card 3: Snooze Analytics
    private val _snoozeRangeStats = MutableStateFlow<SnoozeRangeStats?>(null)
    val snoozeRangeStats: StateFlow<SnoozeRangeStats?> = _snoozeRangeStats.asStateFlow()

    // Independent Card 4: Heatmap
    private val _weeklyHeatmap = MutableStateFlow<List<DayResult>>(emptyList())
    val weeklyHeatmap: StateFlow<List<DayResult>> = _weeklyHeatmap.asStateFlow()

    // Independent Card 5: Mission Breakdown
    private val _missionBreakdown = MutableStateFlow<List<MissionBreakdownItem>>(emptyList())
    val missionBreakdown: StateFlow<List<MissionBreakdownItem>> = _missionBreakdown.asStateFlow()

    // Independent Card 6: Recent Missions
    val recentMissions: StateFlow<List<MissionHistoryEntity>> =
        missionHistoryRepository.getRecentMissions(20)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        // Independent Worker Thread 1: Streak Card Calculation
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val streak = missionHistoryRepository.calculateCurrentStreak()
                _currentStreak.value = streak
            } catch (e: Exception) {
                _currentStreak.value = 0
            }
        }

        // Independent Worker Thread 2: Lifetime Stats Card Calculation (Best Streak, Total Dismissed, Success Rate, Avg Solve time, Total Runs, Total Snoozed)
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val stats = missionHistoryRepository.getLifetimeStats()
                _lifetimeStats.value = stats
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Independent Worker Thread 3: Weekly Heatmap Card Calculation
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val heatmap = missionHistoryRepository.getWeeklyHeatmap()
                _weeklyHeatmap.value = heatmap
            } catch (e: Exception) {
                _weeklyHeatmap.value = emptyList()
            }
        }

        // Independent Worker Thread 4: Mission Breakdown Card Calculation
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val breakdown = missionHistoryRepository.getMissionBreakdown()
                _missionBreakdown.value = breakdown
            } catch (e: Exception) {
                _missionBreakdown.value = emptyList()
            }
        }

        // Independent Worker Thread 5: Snooze Analytics Card Calculation (Default range: Last 7 days)
        val now = System.currentTimeMillis()
        val sevenDaysAgo = now - (7L * 24 * 60 * 60 * 1000)
        updateSnoozeDateRange(sevenDaysAgo, now)
    }

    fun updateSnoozeDateRange(startMs: Long, endMs: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val snoozeStats = missionHistoryRepository.getSnoozeStatsInRange(startMs, endMs)
                _snoozeRangeStats.value = snoozeStats
            } catch (e: Exception) {
                _snoozeRangeStats.value = SnoozeRangeStats(
                    startMs = startMs,
                    endMs = endMs,
                    totalSnoozed = 0,
                    daysInRange = 7,
                    avgSnoozePerDay = 0f
                )
            }
        }
    }
}
