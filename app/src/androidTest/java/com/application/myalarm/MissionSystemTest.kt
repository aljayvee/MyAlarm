package com.application.myalarm

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.myalarm.mission.*
import androidx.test.rule.GrantPermissionRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MissionSystemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    @Test
    fun testMathProblemMission_solvesSuccessfully() {
        var missionCompleted = false

        composeTestRule.setContent {
            MathProblemMission(onMissionComplete = { missionCompleted = true })
        }

        // We have 3 math problems to solve
        for (i in 1..3) {
            var parsedQuestionText = ""
            // Locate the node containing the math expression (contains +, −, or ×)
            val matcher = SemanticsMatcher("MathExpression") { node ->
                val textList = node.config.getOrNull(SemanticsProperties.Text)
                if (textList != null && textList.isNotEmpty()) {
                    val text = textList.first().text
                    text.contains("+") || text.contains("−") || text.contains("×")
                } else {
                    false
                }
            }

            composeTestRule.onNode(matcher).assertExists()
            val semanticsNode = composeTestRule.onNode(matcher).fetchSemanticsNode()
            val textList = semanticsNode.config.getOrNull(SemanticsProperties.Text)
            parsedQuestionText = textList?.first()?.text ?: ""

            assertTrue("Should have parsed a valid question text", parsedQuestionText.isNotEmpty())

            // Solve it
            val answer = solveMathExpression(parsedQuestionText)

            // Enter the answer
            composeTestRule.onNodeWithText("Your answer")
                .performTextInput(answer.toString())

            // Click Submit
            composeTestRule.onNodeWithText("Submit")
                .performClick()
                
            composeTestRule.waitForIdle()
        }

        // After solving 3, the callback should fire
        assertTrue("Mission should be completed", missionCompleted)
    }

    @Test
    fun testAffirmationMission_typesSuccessfully() {
        var missionCompleted = false

        composeTestRule.setContent {
            AffirmationMission(onMissionComplete = { missionCompleted = true })
        }
        val targetAffirmations = listOf(
            "I am grateful for this new day and its opportunities",
            "I choose to be happy, healthy, and positive today",
            "I am capable of achieving all my goals today",
            "I believe in myself and my abilities completely",
            "I am strong, resilient, and ready for anything"
        )

        var foundTarget = ""
        for (aff in targetAffirmations) {
            try {
                composeTestRule.onNodeWithText(aff, substring = true).assertExists()
                foundTarget = aff
                break
            } catch (e: AssertionError) {
                // Ignore and try next
            }
        }

        assertTrue("Should have found a target affirmation on screen", foundTarget.isNotEmpty())

        // Start typing
        composeTestRule.onNodeWithText("Start typing here...")
            .performTextInput(foundTarget)

        composeTestRule.waitForIdle()

        assertTrue("Mission should be completed after typing affirmation", missionCompleted)
    }

    @Test
    fun testTypingMission_typesSuccessfully() {
        var missionCompleted = false

        composeTestRule.setContent {
            TypingMission(onMissionComplete = { missionCompleted = true })
        }
        val targetSentences = listOf(
            "The early bird catches the worm.",
            "A journey of a thousand miles begins with a single step.",
            "Every morning brings new potential, utilize it.",
            "Believe you can and you are halfway there.",
            "Great things never came from comfort zones."
        )

        var foundTarget = ""
        for (sentence in targetSentences) {
            try {
                composeTestRule.onNodeWithText(sentence, substring = true).assertExists()
                foundTarget = sentence
                break
            } catch (e: AssertionError) {
                // Ignore and try next
            }
        }

        assertTrue("Should have found a target sentence on screen", foundTarget.isNotEmpty())

        // Start typing
        composeTestRule.onNodeWithText("Start typing...")
            .performTextInput(foundTarget)

        composeTestRule.waitForIdle()

        assertTrue("Mission should be completed after typing sentence", missionCompleted)
    }

    @Test
    fun testQuoteOfTheDayMission_dismissesAfterDelay() {
        var missionCompleted = false

        composeTestRule.setContent {
            QuoteOfTheDayMission(onMissionComplete = { missionCompleted = true })
        }

        // Verify the delay button is initially active but disabled
        composeTestRule.onNodeWithText("Read for", substring = true).assertExists()

        // Temporarily pause auto-advance to manually advance time in the Compose clock
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(6000L)
        
        // Re-enable auto-advance and wait for recomposition
        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        // Verify that the button is now active with the read text
        composeTestRule.onNodeWithText("I've read this").assertExists()

        // Press the read confirmation button
        composeTestRule.onNodeWithText("I've read this").performClick()
        composeTestRule.waitForIdle()

        assertTrue("Mission should be completed after clicking read confirmation", missionCompleted)
    }

    private fun solveMathExpression(expr: String): Int {
        val cleanExpr = expr.replace("−", "-").replace("×", "*").trim()
        val parts = cleanExpr.split(" ")
        if (parts.size == 3) {
            val a = parts[0].toInt()
            val op = parts[1]
            val b = parts[2].toInt()
            return when (op) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                else -> throw IllegalArgumentException("Unknown operator: $op")
            }
        }
        throw IllegalArgumentException("Could not parse math expression: $expr")
    }

    @Test
    fun testSkyPhotoMission_completesSuccessfully() {
        var missionCompleted = false
        composeTestRule.setContent {
            SkyPhotoMission(onMissionComplete = { missionCompleted = true })
        }
        completeCameraMission("Sky Photo")
        composeTestRule.waitUntil(5000L) { missionCompleted }
    }

    @Test
    fun testMakeYourBedMission_completesSuccessfully() {
        var missionCompleted = false
        composeTestRule.setContent {
            MakeYourBedMission(onMissionComplete = { missionCompleted = true })
        }
        completeCameraMission("Make Your Bed")
        composeTestRule.waitUntil(5000L) { missionCompleted }
    }

    @Test
    fun testObjectHuntMission_completesSuccessfully() {
        var missionCompleted = false
        composeTestRule.setContent {
            ObjectHuntMission(onMissionComplete = { missionCompleted = true })
        }
        completeCameraMission("Object Hunt")
        composeTestRule.waitUntil(5000L) { missionCompleted }
    }

    @Test
    fun testPushUpsMission_completesWithSimulator() {
        var missionCompleted = false
        composeTestRule.setContent {
            PushUpsMission(onMissionComplete = { missionCompleted = true }, isTesting = true)
        }
        // Click "Simulate Rep" 10 times to complete
        for (i in 1..10) {
            composeTestRule.onNodeWithText("Simulate Rep").performClick()
            composeTestRule.waitForIdle()
        }
        assertTrue("Push-Ups mission should complete via simulator", missionCompleted)
    }

    @Test
    fun testShakeMission_completesWithSimulator() {
        var missionCompleted = false
        composeTestRule.setContent {
            ShakeMission(onMissionComplete = { missionCompleted = true }, isTesting = true)
        }
        // Click "Simulate Shake" 30 times to complete
        for (i in 1..30) {
            composeTestRule.onNodeWithText("Simulate Shake").performClick()
            composeTestRule.waitForIdle()
        }
        assertTrue("Shake mission should complete via simulator", missionCompleted)
    }

    @Test
    fun testQRCodeMission_completesWithSimulator() {
        var missionCompleted = false
        composeTestRule.setContent {
            QRCodeMission(targetValue = "TEST_QR_CODE", onMissionComplete = { missionCompleted = true }, isTesting = true)
        }
        composeTestRule.onNodeWithTag("simulate_match_scan_button").performClick()
        composeTestRule.waitForIdle()
        assertTrue("QR Code mission should complete via simulator", missionCompleted)
    }

    @Test
    fun testBarcodeMission_completesWithSimulator() {
        var missionCompleted = false
        composeTestRule.setContent {
            BarcodeMission(targetValue = "TEST_BARCODE", onMissionComplete = { missionCompleted = true }, isTesting = true)
        }
        composeTestRule.onNodeWithTag("simulate_match_scan_button").performClick()
        composeTestRule.waitForIdle()
        assertTrue("Barcode mission should complete via simulator", missionCompleted)
    }

    @Test
    fun testStepCountMission_completesWithSimulator() {
        var missionCompleted = false
        val level = 2
        val targetSteps = level * 5
        composeTestRule.setContent {
            StepCountMission(level = level, onMissionComplete = { missionCompleted = true }, isTesting = true)
        }
        for (i in 1..targetSteps) {
            composeTestRule.onNodeWithTag("simulate_step_button").performClick()
            composeTestRule.waitForIdle()
        }
        composeTestRule.waitUntil(3000L) { missionCompleted }
        assertTrue("Step Count mission should complete via simulator", missionCompleted)
    }

    @Test
    fun testMemoryMission_completesAllRounds() {
        var missionCompleted = false
        composeTestRule.setContent {
            MemoryMission(onMissionComplete = { missionCompleted = true })
        }

        // For each of the 3 rounds
        for (round in 1..3) {
            // Wait until the state is GameState.USER_INPUT (i.e. "Repeat the sequence: 0 / X")
            composeTestRule.waitUntil(15000L) {
                try {
                    composeTestRule.onNodeWithText("Repeat the sequence:", substring = true).assertExists()
                    true
                } catch (e: AssertionError) {
                    false
                }
            }

            // Fetch the Column with testTag memory_target_pattern_X_Y_Z...
            val matcher = SemanticsMatcher("MemoryTargetPattern") { node ->
                val testTag = node.config.getOrNull(SemanticsProperties.TestTag)
                testTag != null && testTag.startsWith("memory_target_pattern_")
            }

            composeTestRule.onNode(matcher).assertExists()
            val semanticsNode = composeTestRule.onNode(matcher).fetchSemanticsNode()
            val testTag = semanticsNode.config.getOrNull(SemanticsProperties.TestTag) ?: ""
            val patternString = testTag.substringAfter("memory_target_pattern_")
            val targetPattern = patternString.split("_").map { it.toInt() }

            // Tap the cells in the targetPattern order
            for (cellIndex in targetPattern) {
                composeTestRule.onNodeWithTag("memory_cell_$cellIndex").performClick()
                composeTestRule.waitForIdle()
            }
        }

        // Wait for final round success animation and completion
        composeTestRule.waitForIdle()
        // Wait up to 5 seconds for callback to fire
        composeTestRule.waitUntil(5000L) { missionCompleted }
        assertTrue("Memory mission should complete after 3 rounds", missionCompleted)
    }

    private fun completeCameraMission(missionName: String) {
        composeTestRule.waitForIdle()
        var clicked = false

        // Check if fallback button "Complete Anyway" exists (due to denied permissions)
        try {
            composeTestRule.onNodeWithText("Complete Anyway").assertExists()
            composeTestRule.onNodeWithText("Complete Anyway").performClick()
            clicked = true
        } catch (e: AssertionError) {
            // Fallback not present
        }

        // Check if "Skip Mission" exists (due to camera initialization failure / emulator environment)
        if (!clicked) {
            try {
                composeTestRule.onNodeWithText("Skip Mission").assertExists()
                composeTestRule.onNodeWithText("Skip Mission").performClick()
                clicked = true
            } catch (e: AssertionError) {
                // Skip not present
            }
        }

        // Check if capture button exists (due to camera ready)
        if (!clicked) {
            try {
                composeTestRule.onNodeWithContentDescription("Capture").assertExists()
                composeTestRule.onNodeWithContentDescription("Capture").performClick()
                clicked = true
            } catch (e: AssertionError) {
                // Capture button not present
            }
        }

        assertTrue("Should have clicked a completion button for $missionName", clicked)
        composeTestRule.waitForIdle()
    }
}
