package com.application.myalarm.ui.alarm

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.myalarm.mission.*
import com.application.myalarm.util.Localizer
import androidx.activity.compose.BackHandler
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private val GradientStart = Color(0xFF1E1E2E)
private val GradientEnd = Color(0xFF0F0F1A)
private val TextWhite = Color(0xFFFFFFFF)
private val GrayText = Color(0xFFB0B0B0)
private val RedDismiss = Color(0xFFE53935)
private val BlueSnooze = Color(0xFF1E88E5)

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
    onSnooze: () -> Unit = {},
    viewModel: AlarmRingingViewModel = viewModel()
) {
    val isSnoozed by viewModel.isSnoozed.collectAsState()

    BackHandler(enabled = true) {
        // Intercept back button to prevent accidental exiting
    }

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

    val completeCallback = {
        val elapsed = viewModel.getElapsedTimeMs()
        onDismiss(elapsed)
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = formattedTime,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = GrayText
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Middle Section (Mission Container or Snoozed View)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (isSnoozed) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlueSnooze.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Snooze,
                            contentDescription = "Snoozed",
                            tint = BlueSnooze,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = Localizer.t("Alarm Snoozed"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Localizer.t("Will ring again in 5 minutes..."),
                            fontSize = 14.sp,
                            color = GrayText
                        )
                    }
                }
            } else if (missionType.isNotBlank() && missionType != "NONE") {
                when (missionType) {
                    "MATH_PROBLEM" -> MathProblemMission(onMissionComplete = completeCallback)
                    "SHAKE" -> ShakeMission(
                        onMissionComplete = completeCallback,
                        difficultyLevel = stepCountLevel,
                        isTesting = false
                    )
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
                        DefaultWakeupGraphic()
                    }
                }
            } else {
                DefaultWakeupGraphic()
            }
        }

        // Bottom Controls: VISIBLE IN ALL MISSIONS!
        if (!isSnoozed) {
            Spacer(modifier = Modifier.height(12.dp))
            StandardAlarmRingingControls(
                viewModel = viewModel,
                onDismiss = completeCallback,
                onSnooze = {
                    viewModel.triggerSnooze()
                    onSnooze()
                }
            )
        }
    }
}

@Composable
private fun DefaultWakeupGraphic() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AlarmOn,
            contentDescription = "Ringing",
            tint = Color(0xFFFF8C00),
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = Localizer.t("Time to Wake Up!"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
    }
}

@Composable
private fun StandardAlarmRingingControls(
    viewModel: AlarmRingingViewModel,
    onDismiss: () -> Unit,
    onSnooze: () -> Unit
) {
    val holdProgressMs by viewModel.holdProgressMs.collectAsState()
    var isHolding by remember { mutableStateOf(false) }

    val progressFraction = (holdProgressMs.toFloat() / AlarmRingingViewModel.REQUIRED_HOLD_DURATION_MS).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "holdProgress")

    // Handle press-and-hold loop
    LaunchedEffect(isHolding) {
        if (isHolding) {
            val stepMs = 100L
            while (isHolding && viewModel.holdProgressMs.value < AlarmRingingViewModel.REQUIRED_HOLD_DURATION_MS) {
                delay(stepMs)
                if (isHolding) {
                    val reached = viewModel.updateHoldProgress(stepMs)
                    if (reached) {
                        onDismiss()
                        break
                    }
                }
            }
        } else {
            // Releasing prematurely immediately resets progress back to 0!
            viewModel.resetHoldProgress()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Slide-to-Snooze Track (Compact height, full width, with slide background fill)
        SlideToSnoozeBar(onSnooze = onSnooze)

        // 25-Second Hold-to-Dismiss Button with Large Countdown Font
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3A)),
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isHolding = true
                            tryAwaitRelease()
                            isHolding = false
                        }
                    )
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Background progress fill
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .background(RedDismiss)
                        .align(Alignment.CenterStart)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isHolding) {
                        val remainingSec = ((AlarmRingingViewModel.REQUIRED_HOLD_DURATION_MS - holdProgressMs) / 1000.0).coerceAtLeast(0.0)
                        Text(
                            text = String.format("%.1fs", remainingSec),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFD54F)
                        )
                        Text(
                            text = Localizer.t("Keep holding to stop alarm"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Hold to Stop",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Localizer.t("HOLD 25s TO STOP ALARM"),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = Localizer.t("Hold down continuously to dismiss"),
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SlideToSnoozeBar(
    onSnooze: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var containerWidthPx by remember { mutableStateOf(0f) }

    val density = LocalDensity.current
    val thumbSizeDp = 48.dp
    val thumbSizePx = with(density) { thumbSizeDp.toPx() }

    val maxDragPx = (containerWidthPx - thumbSizePx).coerceAtLeast(1f)
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        label = "snoozeSlide"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1E293B))
            .onGloballyPositioned { coordinates ->
                containerWidthPx = coordinates.size.width.toFloat()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Dynamic background fill color behind the sliding thumb
        val fillWidthFraction = if (containerWidthPx > 0) {
            ((animatedOffset + thumbSizePx) / containerWidthPx).coerceIn(0f, 1f)
        } else 0f

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillWidthFraction)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1565C0), BlueSnooze)
                    )
                )
        )

        // Track text hint
        Text(
            text = Localizer.t("Slide right to Snooze (5 mins) ➔"),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Draggable Snooze Pill Thumb
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .size(thumbSizeDp)
                .clip(CircleShape)
                .background(BlueSnooze)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newOffset = (offsetX + delta).coerceIn(0f, maxDragPx)
                        offsetX = newOffset
                    },
                    onDragStopped = {
                        if (offsetX >= maxDragPx * 0.75f) {
                            onSnooze()
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Snooze,
                contentDescription = "Snooze Thumb",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
