package com.application.myalarm.ui.alarm

import androidx.lifecycle.ViewModel

class AlarmRingingViewModel : ViewModel() {

    private var startTimeMs: Long = System.currentTimeMillis()

    fun resetStartTime() {
        startTimeMs = System.currentTimeMillis()
    }

    fun getElapsedTimeMs(): Long {
        return System.currentTimeMillis() - startTimeMs
    }
}
