package com.application.myalarm

import com.application.myalarm.mission.ShakeMissionConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShakeMissionTest {

    @Test
    fun testShakeLevelConfigurations() {
        // Level 1 = 40 shakes, Easy
        val level1 = ShakeMissionConfig.getConfig(1)
        assertEquals(40, level1.requiredShakes)
        assertEquals(9.5f, level1.sensitivityThreshold, 0.01f)
        assertEquals("Easy", level1.labelKey)

        // Level 2 = 50 shakes, Simple
        val level2 = ShakeMissionConfig.getConfig(2)
        assertEquals(50, level2.requiredShakes)
        assertEquals(11.0f, level2.sensitivityThreshold, 0.01f)
        assertEquals("Simple", level2.labelKey)

        // Level 3 = 60 shakes, Moderate (Default)
        val level3 = ShakeMissionConfig.getConfig(3)
        assertEquals(60, level3.requiredShakes)
        assertEquals(12.5f, level3.sensitivityThreshold, 0.01f)
        assertEquals("Moderate", level3.labelKey)

        // Level 4 = 80 shakes, Hard
        val level4 = ShakeMissionConfig.getConfig(4)
        assertEquals(80, level4.requiredShakes)
        assertEquals(14.5f, level4.sensitivityThreshold, 0.01f)
        assertEquals("Hard", level4.labelKey)

        // Level 5 = 120 shakes, Extreme
        val level5 = ShakeMissionConfig.getConfig(5)
        assertEquals(120, level5.requiredShakes)
        assertEquals(17.0f, level5.sensitivityThreshold, 0.01f)
        assertEquals("Extreme", level5.labelKey)
    }

    @Test
    fun testDefaultLevelFallback() {
        // Invalid or out-of-bounds levels fallback to Level 3 (60 shakes, Moderate)
        val defaultFallback = ShakeMissionConfig.getConfig(-1)
        assertEquals(3, defaultFallback.level)
        assertEquals(60, defaultFallback.requiredShakes)
        assertEquals(12.5f, defaultFallback.sensitivityThreshold, 0.01f)
        assertEquals("Moderate", defaultFallback.labelKey)

        val upperFallback = ShakeMissionConfig.getConfig(99)
        assertEquals(3, upperFallback.level)
        assertEquals(60, upperFallback.requiredShakes)
    }

    @Test
    fun testShakeValidationThresholdAndInterval() {
        val config = ShakeMissionConfig.getConfig(3) // Threshold = 12.5f
        val threshold = config.sensitivityThreshold
        val minInterval = 300L
        val lastShakeTime = 1000L

        // Valid Shake: Magnitude above threshold AND time diff > 300ms
        assertTrue(ShakeMissionConfig.isShakeValid(13.0f, threshold, lastShakeTime, 1400L, minInterval))

        // Invalid Shake: Magnitude below threshold
        assertFalse(ShakeMissionConfig.isShakeValid(10.0f, threshold, lastShakeTime, 1400L, minInterval))

        // Invalid Shake: Time diff too short (200ms < 300ms)
        assertFalse(ShakeMissionConfig.isShakeValid(15.0f, threshold, lastShakeTime, 1200L, minInterval))
    }

    @Test
    fun testProgressRatioAndCompletion() {
        val config = ShakeMissionConfig.getConfig(1) // 40 shakes required
        val target = config.requiredShakes
        
        val countHalf = 20
        val progressHalf = countHalf.toFloat() / target.toFloat()
        assertEquals(0.5f, progressHalf, 0.001f)
        assertFalse(countHalf >= target)

        val countComplete = 40
        val progressComplete = countComplete.toFloat() / target.toFloat()
        assertEquals(1.0f, progressComplete, 0.001f)
        assertTrue(countComplete >= target)
    }
}
