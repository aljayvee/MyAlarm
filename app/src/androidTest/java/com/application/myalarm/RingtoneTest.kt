package com.application.myalarm

import android.content.Context
import android.media.MediaPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RingtoneTest {

    private val soundMappings = mapOf(
        "Basic" to "basic_alarm",
        "Alarm Clock" to "alarm_clock",
        "Bugle Tune" to "bugle_tune",
        "Medium Bell" to "medium_bell_ringing_near",
        "Short Beeps" to "beep_short",
        "Clown Horn" to "clown_horn",
        "Wake Up" to "wake_up_alarm",
        "Nature" to "nature_alarm",
        "Digital Watch" to "digital_watch_alarm",
        "Spaceship" to "spaceship_alarm",
        "Dosimeter" to "dosimeter_alarm",
        "Phone Alerts" to "phone_alerts_and_rings",
        "Computer Sounds" to "assorted_computer_sounds",
        "Alien Beam" to "alien_beam",
        "Electric Ring" to "electric_ring_long",
        "High Low Sweep" to "high_to_low_sweep"
    )

    @Test
    fun testAllRingtonesExistAndArePlayable() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = context.packageName

        for ((soundName, resName) in soundMappings) {
            // 1. Verify resource ID can be resolved
            val resId = context.resources.getIdentifier(resName, "raw", packageName)
            assertNotEquals("Ringtone '$soundName' with resource name '$resName' could not be resolved (ID was 0)", 0, resId)

            // 2. Try to instantiate MediaPlayer with this resource to verify playability and volume setting
            var mediaPlayer: MediaPlayer? = null
            try {
                mediaPlayer = MediaPlayer.create(context, resId)
                assertNotNull("MediaPlayer failed to create for ringtone '$soundName' (resId: $resId)", mediaPlayer)
                
                // Test volume levels to ensure no exceptions are thrown when setting custom volume
                mediaPlayer.setVolume(0.5f, 0.5f)
                mediaPlayer.setVolume(1.0f, 1.0f)
                mediaPlayer.setVolume(0.0f, 0.0f)
            } catch (e: Exception) {
                fail("Exception thrown while creating MediaPlayer or setting volume for ringtone '$soundName': ${e.message}")
            } finally {
                mediaPlayer?.release()
            }
        }
    }
}
