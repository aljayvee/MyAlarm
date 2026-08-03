package com.application.myalarm.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val sensorManager: SensorManager,
    private val threshold: Float = 12f,
    private val onShake: () -> Unit
) : SensorEventListener {

    private var lastUpdate: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f
    private var isRegistered: Boolean = false

    fun start() {
        if (isRegistered) return
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
            isRegistered = true
        }
    }

    fun stop() {
        if (!isRegistered) return
        sensorManager.unregisterListener(this)
        isRegistered = false
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdate < 100) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        if (lastUpdate != 0L) {
            val deltaX = x - lastX
            val deltaY = y - lastY
            val deltaZ = z - lastZ
            val speed = sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)

            if (speed > threshold) {
                onShake()
            }
        }

        lastX = x
        lastY = y
        lastZ = z
        lastUpdate = currentTime
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // No-op
    }
}
