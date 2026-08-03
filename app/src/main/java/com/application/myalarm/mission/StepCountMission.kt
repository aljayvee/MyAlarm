package com.application.myalarm.mission

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import com.application.myalarm.util.Localizer

private val AccentOrange = Color(0xFFFF8C00)

@Composable
fun StepCountMission(
    level: Int,
    onMissionComplete: () -> Unit,
    isTesting: Boolean = false
) {
    val context = LocalContext.current
    val targetSteps = remember(level) { level * 5 }
    var currentSteps by remember { mutableIntStateOf(0) }
    var stepDetected by remember { mutableStateOf(false) }

    LaunchedEffect(currentSteps) {
        if (currentSteps >= targetSteps) {
            delay(1000)
            onMissionComplete()
        }
    }

    // Register Accelerometer sensor for step detection
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var lastMagnitude = 9.8f
        var isAboveThreshold = false
        var lastStepTime = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null || currentSteps >= targetSteps) return

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val magnitude = sqrt(x * x + y * y + z * z)

                // Simple exponential moving average to filter noise
                val filteredMagnitude = 0.8f * magnitude + 0.2f * lastMagnitude
                lastMagnitude = filteredMagnitude

                val currentTime = System.currentTimeMillis()
                if (currentTime - lastStepTime < 400) return // Enforce debounce time between steps

                // Peak and valley detection
                if (filteredMagnitude > 12.0f && !isAboveThreshold) {
                    isAboveThreshold = true
                } else if (filteredMagnitude < 9.2f && isAboveThreshold) {
                    isAboveThreshold = false
                    currentSteps++
                    lastStepTime = currentTime
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (accelerometer != null) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
        contentAlignment = Alignment.Center
    ) {
        if (currentSteps < targetSteps) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsRun,
                            contentDescription = Localizer.t("Step Count"),
                            modifier = Modifier.size(48.dp),
                            tint = AccentOrange
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = Localizer.t("Walk to dismiss"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = Localizer.t("Steps count level is set to {level}. Walk around until the steps are complete.")
                                .replace("{level}", level.toString()),
                            fontSize = 14.sp,
                            color = Color(0xFFBDBDBD),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Steps Counter Display
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { currentSteps.toFloat() / targetSteps.toFloat() },
                        modifier = Modifier.fillMaxSize(),
                        color = AccentOrange,
                        strokeWidth = 12.dp,
                        trackColor = Color(0xFF2D2D2D)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$currentSteps",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "/ $targetSteps",
                            fontSize = 18.sp,
                            color = Color(0xFFBDBDBD)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                if (isTesting) {
                    // Test simulation button
                    Button(
                        onClick = {
                            if (currentSteps < targetSteps) currentSteps++
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                        modifier = Modifier.testTag("simulate_step_button")
                    ) {
                        Text(Localizer.t("Simulate Step"), color = Color.White)
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = Localizer.t("Success"),
                    modifier = Modifier.size(96.dp),
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = Localizer.t("Goal Reached!"),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = Localizer.t("Mission complete"),
                    fontSize = 16.sp,
                    color = Color(0xFFBDBDBD)
                )
            }
        }
    }
}
