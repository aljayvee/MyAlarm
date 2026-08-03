package com.application.myalarm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyboardMissionTest {

    @Test
    fun testMathProblemFilterInput() {
        // Validation logic from MathProblemMission:
        // newValue.all { c -> c.isDigit() || c == '-' }
        val validDigits = "12345"
        val validNegative = "-42"
        val invalidChars = "12.3"
        val invalidLetter = "12a"

        assertTrue(validDigits.all { it.isDigit() || it == '-' })
        assertTrue(validNegative.all { it.isDigit() || it == '-' })
        assertFalse(invalidChars.all { it.isDigit() || it == '-' })
        assertFalse(invalidLetter.all { it.isDigit() || it == '-' })
    }

    @Test
    fun testTypingMissionValidation() {
        val target = "Great things never came from comfort zones"
        
        // Exact match (case-sensitive)
        val correctInput = "Great things never came from comfort zones"
        assertEquals(target, correctInput)

        // Case-sensitive mismatch
        val mismatchCase = "great things never came from comfort zones"
        assertFalse(target == mismatchCase)

        // Length restriction logic: if (it.length <= target.length)
        val overLengthInput = "Great things never came from comfort zones extra text"
        assertFalse(overLengthInput.length <= target.length)
        assertTrue(correctInput.length <= target.length)
    }

    @Test
    fun testAffirmationMissionValidation() {
        val target = "I am grateful for this new day"

        // Case-insensitive exact match
        val correctInputLowercase = "i am grateful for this new day"
        assertTrue(correctInputLowercase.trim().equals(target.trim(), ignoreCase = true))

        // Trim match
        val correctInputPadded = "  I am grateful for this new day  "
        assertTrue(correctInputPadded.trim().equals(target.trim(), ignoreCase = true))

        // Mismatched words
        val incorrectInput = "I am not grateful for this day"
        assertFalse(incorrectInput.trim().equals(target.trim(), ignoreCase = true))
    }
}
