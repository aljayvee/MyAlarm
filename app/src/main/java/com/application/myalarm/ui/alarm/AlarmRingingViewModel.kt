package com.application.myalarm.ui.alarm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlarmRingingViewModel : ViewModel() {

    companion object {
        const val REQUIRED_HOLD_DURATION_MS = 25_000L
    }

    private var startTimeMs: Long = System.currentTimeMillis()

    private val _holdProgressMs = MutableStateFlow(0L)
    val holdProgressMs: StateFlow<Long> = _holdProgressMs.asStateFlow()

    private val _isSnoozed = MutableStateFlow(false)
    val isSnoozed: StateFlow<Boolean> = _isSnoozed.asStateFlow()

    fun resetStartTime() {
        startTimeMs = System.currentTimeMillis()
        _holdProgressMs.value = 0L
        _isSnoozed.value = false
    }

    fun getElapsedTimeMs(): Long {
        return System.currentTimeMillis() - startTimeMs
    }

    fun updateHoldProgress(addedTimeMs: Long): Boolean {
        val newProgress = (_holdProgressMs.value + addedTimeMs).coerceAtMost(REQUIRED_HOLD_DURATION_MS)
        _holdProgressMs.value = newProgress
        return newProgress >= REQUIRED_HOLD_DURATION_MS
    }

    fun resetHoldProgress() {
        _holdProgressMs.value = 0L
    }

    fun triggerSnooze() {
        _isSnoozed.value = true
    }
}
