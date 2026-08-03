package com.application.myalarm.mission

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.util.Locale

sealed class SkyVerificationResult {
    object VerifiedSky : SkyVerificationResult()
    data class RejectedBedDetected(val label: String) : SkyVerificationResult()
    data class RejectedIndoorScene(val reason: String) : SkyVerificationResult()
}

object SkyPhotoAnalyzer {

    private const val TAG = "SkyPhotoAnalyzer"

    // Labels that explicitly indicate indoor bedroom/bed structures (Filipino & global homes/apartments/boarding houses)
    val BED_REJECTION_LABELS = setOf(
        "bed", "bedroom", "mattress", "pillow", "blanket", "furniture",
        "ceiling", "wall", "room", "linens", "couch", "bedding",
        "comfort", "interior design", "bed frame", "sheet", "duvet", "curtain"
    )

    // Labels that explicitly indicate morning/daytime sky features
    val SKY_ACCEPT_LABELS = setOf(
        "sky", "cloud", "daytime", "sunlight", "atmosphere", "horizon",
        "cumulus", "blue sky", "sun", "sunshine", "outer space", "astronomical object"
    )

    fun isAmAlarm(hour: Int): Boolean {
        return hour in 0..11
    }

    /**
     * Pure evaluator for ML Kit label strings.
     */
    fun evaluateLabels(labels: List<String>): SkyVerificationResult {
        val lowercaseLabels = labels.map { it.lowercase(Locale.US) }

        // 1. Check if any bed/bedroom furniture is detected
        for (label in lowercaseLabels) {
            if (BED_REJECTION_LABELS.any { label.contains(it) }) {
                return SkyVerificationResult.RejectedBedDetected(label)
            }
        }

        // 2. Check if any sky/daytime feature is detected
        for (label in lowercaseLabels) {
            if (SKY_ACCEPT_LABELS.any { label.contains(it) }) {
                return SkyVerificationResult.VerifiedSky
            }
        }

        return SkyVerificationResult.RejectedIndoorScene("No outdoor sky features detected in photo.")
    }

    /**
     * Evaluates Bitmap top 60% pixels for daylight luminance and sky-blue color ratios.
     */
    fun evaluateBitmapSkyRatio(bitmap: Bitmap): Boolean {
        val width = bitmap.width
        val height = (bitmap.height * 0.6).toInt().coerceAtLeast(1)
        
        var totalPixels = 0
        var skyBlueOrDaylightPixels = 0

        // Sample every 4th pixel for speed
        for (x in 0 until width step 4) {
            for (y in 0 until height step 4) {
                val pixel = bitmap.getPixel(x, y)
                val red = (pixel shr 16) and 0xFF
                val green = (pixel shr 8) and 0xFF
                val blue = pixel and 0xFF

                totalPixels++

                // Sky blue condition: Blue is dominant (B > R * 1.15) OR high bright daylight (R > 180, G > 180, B > 180)
                val isSkyBlue = blue > (red * 1.15f) && green > (red * 0.85f) && blue > 90
                val isBrightDaylight = red > 180 && green > 180 && blue > 180

                if (isSkyBlue || isBrightDaylight) {
                    skyBlueOrDaylightPixels++
                }
            }
        }

        if (totalPixels == 0) return false
        val ratio = skyBlueOrDaylightPixels.toFloat() / totalPixels.toFloat()
        return ratio >= 0.35f
    }

    fun analyzePhoto(
        context: Context,
        photoFile: File,
        onResult: (SkyVerificationResult) -> Unit
    ) {
        try {
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            if (bitmap == null) {
                onResult(SkyVerificationResult.RejectedIndoorScene("Unable to read captured image."))
                return
            }

            val image = InputImage.fromBitmap(bitmap, 0)
            val labeler = ImageLabeling.getClient(
                ImageLabelerOptions.Builder()
                    .setConfidenceThreshold(0.4f)
                    .build()
            )

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val detectedTexts = labels.map { it.text }
                    Log.d(TAG, "ML Kit Labels detected: $detectedTexts")

                    val labelResult = evaluateLabels(detectedTexts)
                    if (labelResult is SkyVerificationResult.VerifiedSky) {
                        onResult(SkyVerificationResult.VerifiedSky)
                    } else if (labelResult is SkyVerificationResult.RejectedBedDetected) {
                        onResult(labelResult)
                    } else {
                        // Fallback to Bitmap color analysis
                        val passesColorAnalysis = evaluateBitmapSkyRatio(bitmap)
                        if (passesColorAnalysis) {
                            onResult(SkyVerificationResult.VerifiedSky)
                        } else {
                            onResult(labelResult)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "ML Kit analysis failed, falling back to bitmap color analysis", e)
                    val passesColorAnalysis = evaluateBitmapSkyRatio(bitmap)
                    if (passesColorAnalysis) {
                        onResult(SkyVerificationResult.VerifiedSky)
                    } else {
                        onResult(SkyVerificationResult.RejectedIndoorScene("Indoor scene or low light detected."))
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during photo analysis", e)
            onResult(SkyVerificationResult.RejectedIndoorScene("Analysis failed. Please try again."))
        }
    }
}
