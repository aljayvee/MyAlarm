package com.application.myalarm.mission

import com.application.myalarm.util.Localizer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

private val AccentOrange = Color(0xFFFF8C00)
private val LightOrange = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)

@Composable
fun PushUpsMission(
    onMissionComplete: () -> Unit,
    isTesting: Boolean = false
) {
    val context = LocalContext.current
    val targetReps = 10
    var repCount by remember { mutableIntStateOf(0) }
    
    // Status text to show current state
    var instructionText by remember { mutableStateOf("Place phone face-down on the floor under your chest, then start push-ups.") }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    LaunchedEffect(repCount) {
        if (repCount >= targetReps) {
            onMissionComplete()
        }
    }

    DisposableEffect(Unit) {
        var lastRepTime = 0L
        val minIntervalMs = 1200L
        var restingZ = 0f
        var isCalibrated = false
        var hasTriggeredPeak = false

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
                
                val z = event.values[2] // Z axis acceleration
                
                if (!isCalibrated) {
                    restingZ = z
                    isCalibrated = true
                    return
                }

                val zChange = z - restingZ
                
                // If Z acceleration deviates significantly (more than 3 m/s2)
                if (abs(zChange) > 3.0f) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastRepTime > minIntervalMs) {
                        if (!hasTriggeredPeak) {
                            hasTriggeredPeak = true
                        }
                    }
                } else if (hasTriggeredPeak) {
                    // When Z acceleration returns closer to resting, complete the rep
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastRepTime > minIntervalMs) {
                        repCount = (repCount + 1).coerceAtMost(targetReps)
                        lastRepTime = currentTime
                        hasTriggeredPeak = false
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        } else {
            instructionText = "Accelerometer not detected. Tap the screen to count push-ups manually."
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val progress = remember(repCount) {
        repCount.toFloat() / targetReps.toFloat()
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "PushUpsProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .clickable(enabled = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
                // Manual fallback tap behavior
                if (repCount < targetReps) {
                    repCount++
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = Localizer.t("Push-Ups Mission"),
                tint = AccentOrange,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = Localizer.t("Push-Ups Mission"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Localizer.t(instructionText),
                fontSize = 14.sp,
                color = SubtitleGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    color = AccentOrange,
                    strokeWidth = 12.dp,
                    trackColor = LightOrange,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$repCount",
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = Localizer.t("of {target} reps").replace("{target}", targetReps.toString()),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = SubtitleGray
                    )
                }
            }

            if (isTesting) {
                // Developer cheat trigger/test helper (in case testing on emulator with no accelerometer)
                Spacer(modifier = Modifier.height(32.dp))
                TextButton(
                    onClick = {
                        if (repCount < targetReps) {
                            repCount++
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = AccentOrange)
                ) {
                    Text(Localizer.t("Simulate Rep"))
                }
            }
        }
    }
}
