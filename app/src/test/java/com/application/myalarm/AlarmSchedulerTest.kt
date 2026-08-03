package com.application.myalarm

import com.application.myalarm.alarm.AlarmScheduler
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class AlarmSchedulerTest {

    @Test
    fun `one shot alarm calculates valid future trigger time`() {
        val now = System.currentTimeMillis()
        val triggerTime = AlarmScheduler.calculateNextTriggerTime(hour = 8, minute = 30, repeatDays = 0)
        assertTrue(triggerTime > now)
    }

    @Test
    fun `repeating alarm on weekday bitmask calculates valid future trigger time`() {
        val now = System.currentTimeMillis()
        // Bitmask 0b0011111 (Mon-Fri)
        val repeatDays = (1 shl 0) or (1 shl 1) or (1 shl 2) or (1 shl 3) or (1 shl 4)
        val triggerTime = AlarmScheduler.calculateNextTriggerTime(hour = 7, minute = 0, repeatDays = repeatDays)
        
        assertTrue(triggerTime > now)

        val cal = Calendar.getInstance().apply { timeInMillis = triggerTime }
        assertEquals(7, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, cal.get(Calendar.MINUTE))
    }

    @Test
    fun `midnight boundary trigger time calculation`() {
        val triggerTime = AlarmScheduler.calculateNextTriggerTime(hour = 0, minute = 0, repeatDays = 0)
        val cal = Calendar.getInstance().apply { timeInMillis = triggerTime }
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, cal.get(Calendar.MINUTE))
    }
}
