package com.application.myalarm.ui.alarm

import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.myalarm.mission.*
import androidx.activity.compose.BackHandler

private val GradientStart = Color(0xFF2D2D2D)
private val GradientEnd = Color(0xFF121212)
private val TextWhite = Color(0xFFFFFFFF)
private val GrayText = Color(0xFFB0B0B0)

@Composable
fun AlarmRingingScreen(
    alarmId: Long,
    hour: Int,
    minute: Int,
    label: String,
    missionType: String,
    stepCountLevel: Int,
    scannedCodeValue: String,
    onDismiss: (durationMs: Long) -> Unit,
    viewModel: AlarmRingingViewModel = viewModel()
) {
    BackHandler(enabled = true) {
        // Intercept back button and prevent the user from going back
    }

    // Reset start time when first composed
    LaunchedEffect(alarmId) {
        viewModel.resetStartTime()
    }

    val formattedTime = remember(hour, minute) {
        val amPm = if (hour >= 12) "PM" else "AM"
        val hour12 = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val minStr = String.format("%02d", minute)
        "$hour12:$minStr $amPm"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemGestureExclusion()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formattedTime,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = GrayText
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Mission Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            contentAlignment = Alignment.Center
        ) {
            val completeCallback = {
                val elapsed = viewModel.getElapsedTimeMs()
                onDismiss(elapsed)
            }

            when (missionType) {
                "MATH_PROBLEM" -> MathProblemMission(onMissionComplete = completeCallback)
                "SHAKE" -> ShakeMission(onMissionComplete = completeCallback, isTesting = false)
                "SKY_PHOTO" -> SkyPhotoMission(onMissionComplete = completeCallback)
                "MAKE_YOUR_BED" -> MakeYourBedMission(onMissionComplete = completeCallback)
                "OBJECT_HUNT" -> ObjectHuntMission(onMissionComplete = completeCallback)
                "QUOTE_OF_THE_DAY" -> QuoteOfTheDayMission(onMissionComplete = completeCallback)
                "AFFIRMATION" -> AffirmationMission(onMissionComplete = completeCallback)
                "PUSH_UPS" -> PushUpsMission(onMissionComplete = completeCallback, isTesting = false)
                "MEMORY" -> MemoryMission(onMissionComplete = completeCallback)
                "TYPING" -> TypingMission(onMissionComplete = completeCallback)
                "QR_CODE" -> QRCodeMission(targetValue = scannedCodeValue, onMissionComplete = completeCallback, isTesting = false)
                "BARCODE" -> BarcodeMission(targetValue = scannedCodeValue, onMissionComplete = completeCallback, isTesting = false)
                "STEP_COUNT" -> StepCountMission(level = stepCountLevel, onMissionComplete = completeCallback, isTesting = false)
                else -> {
                    // Fallback to auto-dismiss if unknown mission
                    LaunchedEffect(Unit) {
                        completeCallback()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
