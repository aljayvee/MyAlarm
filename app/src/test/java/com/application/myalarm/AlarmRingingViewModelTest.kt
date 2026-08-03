package com.application.myalarm

import com.application.myalarm.ui.alarm.AlarmRingingViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AlarmRingingViewModelTest {

    private lateinit var viewModel: AlarmRingingViewModel

    @Before
    fun setUp() {
        viewModel = AlarmRingingViewModel()
    }

    @Test
    fun `initial hold progress is zero`() {
        assertEquals(0L, viewModel.holdProgressMs.value)
        assertFalse(viewModel.isSnoozed.value)
    }

    @Test
    fun `incremental hold progress does not complete before 25 seconds`() {
        val completed = viewModel.updateHoldProgress(10_000L)
        assertFalse(completed)
        assertEquals(10_000L, viewModel.holdProgressMs.value)
    }

    @Test
    fun `premature release resets hold progress back to zero`() {
        viewModel.updateHoldProgress(20_000L)
        assertEquals(20_000L, viewModel.holdProgressMs.value)

        // Simulate user releasing hold before 25s
        viewModel.resetHoldProgress()
        assertEquals(0L, viewModel.holdProgressMs.value)
    }

    @Test
    fun `continuous 25 second hold completes successfully`() {
        val completed1 = viewModel.updateHoldProgress(15_000L)
        assertFalse(completed1)

        val completed2 = viewModel.updateHoldProgress(10_000L)
        assertTrue(completed2)
        assertEquals(AlarmRingingViewModel.REQUIRED_HOLD_DURATION_MS, viewModel.holdProgressMs.value)
    }

    @Test
    fun `triggering snooze updates snooze state`() {
        assertFalse(viewModel.isSnoozed.value)
        viewModel.triggerSnooze()
        assertTrue(viewModel.isSnoozed.value)
    }
}
