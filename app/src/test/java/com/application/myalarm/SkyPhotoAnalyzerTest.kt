package com.application.myalarm

import com.application.myalarm.mission.SkyPhotoAnalyzer
import com.application.myalarm.mission.SkyVerificationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SkyPhotoAnalyzerTest {

    @Test
    fun testAMRestrictionLogic() {
        // Morning AM Hours (0..11)
        assertTrue(SkyPhotoAnalyzer.isAmAlarm(0))  // 12 AM Midnight
        assertTrue(SkyPhotoAnalyzer.isAmAlarm(6))  // 6 AM
        assertTrue(SkyPhotoAnalyzer.isAmAlarm(11)) // 11 AM

        // Afternoon/Night PM Hours (12..23)
        assertFalse(SkyPhotoAnalyzer.isAmAlarm(12)) // 12 PM Noon
        assertFalse(SkyPhotoAnalyzer.isAmAlarm(18)) // 6 PM
        assertFalse(SkyPhotoAnalyzer.isAmAlarm(23)) // 11 PM
    }

    @Test
    fun testLabelRejectionForBedStructures() {
        // Sample labels containing bed or bedroom furniture
        val bedLabels = listOf("Bed", "Mattress", "Pillow", "Comfortable")
        val resultBed = SkyPhotoAnalyzer.evaluateLabels(bedLabels)
        assertTrue(resultBed is SkyVerificationResult.RejectedBedDetected)
        assertEquals("bed", (resultBed as SkyVerificationResult.RejectedBedDetected).label)

        val ceilingLabels = listOf("Ceiling", "Wall", "Lighting")
        val resultCeiling = SkyPhotoAnalyzer.evaluateLabels(ceilingLabels)
        assertTrue(resultCeiling is SkyVerificationResult.RejectedBedDetected)
        assertEquals("ceiling", (resultCeiling as SkyVerificationResult.RejectedBedDetected).label)

        val furnitureLabels = listOf("Room", "Interior design", "Furniture", "Linens")
        val resultFurniture = SkyPhotoAnalyzer.evaluateLabels(furnitureLabels)
        assertTrue(resultFurniture is SkyVerificationResult.RejectedBedDetected)
    }

    @Test
    fun testLabelAcceptanceForSky() {
        // Sample labels containing outdoor sky / morning features
        val skyLabels = listOf("Sky", "Cloud", "Daytime", "Sunlight")
        val resultSky = SkyPhotoAnalyzer.evaluateLabels(skyLabels)
        assertTrue(resultSky is SkyVerificationResult.VerifiedSky)

        val blueSkyLabels = listOf("Blue sky", "Horizon", "Atmosphere")
        val resultBlueSky = SkyPhotoAnalyzer.evaluateLabels(blueSkyLabels)
        assertTrue(resultBlueSky is SkyVerificationResult.VerifiedSky)
    }

    @Test
    fun testLabelRejectionForAmbiguousIndoorScene() {
        // Unrelated indoor objects without sky or bed
        val randomIndoorLabels = listOf("Laptop", "Table", "Coffee cup")
        val result = SkyPhotoAnalyzer.evaluateLabels(randomIndoorLabels)
        assertTrue(result is SkyVerificationResult.RejectedIndoorScene)
    }
}
