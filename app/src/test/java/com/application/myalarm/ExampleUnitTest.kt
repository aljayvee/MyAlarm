package com.application.myalarm

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDayInitials() {
        val localizer = com.application.myalarm.util.Localizer
        for (lang in com.application.myalarm.util.Localizer.Language.values()) {
            localizer.currentLanguage = lang
            val translation = localizer.t("Day_Mon_Initial")
            println("Language ${lang.name} (${lang.code}): Localizer.t(Day_Mon_Initial) = $translation")
            // Assert that translation is not key itself
            assertNotEquals("Day_Mon_Initial", translation)
        }
    }

    @Test
    fun testDefaultLanguage() {
        val localizer = com.application.myalarm.util.Localizer
        // Explicitly set language to English to avoid side-effects from other tests
        localizer.currentLanguage = com.application.myalarm.util.Localizer.Language.ENGLISH
        assertEquals("M", localizer.t("Day_Mon_Initial"))
    }

    @Test
    fun testLocalizerNewlineBehavior() {
        // Import Localizer and its Language
        val localizer = com.application.myalarm.util.Localizer
        
        // Let's test English first
        localizer.currentLanguage = com.application.myalarm.util.Localizer.Language.ENGLISH
        
        val qrKey = "Scan the registered QR code matching:\n\"{value}\""
        val barKey = "Scan the registered barcode matching:\n\"{value}\""
        
        val qrResEn = localizer.t(qrKey)
        val barResEn = localizer.t(barKey)
        
        // Assert English returns the exact key
        assertEquals("English lookup for QR key should return the key itself", qrKey, qrResEn)
        assertEquals("English lookup for Barcode key should return the key itself", barKey, barResEn)
        
        // Assert they contain actual newline character, not literal '\\n'
        assertFalse("English QR key should not contain literal \\n text", qrResEn.contains("\\n"))
        assertTrue("English QR key should contain a real newline character", qrResEn.contains("\n"))
        
        // Let's test Chinese Simplified
        localizer.currentLanguage = com.application.myalarm.util.Localizer.Language.CHINESE_SIMPLIFIED
        val qrResZh = localizer.t(qrKey)
        val barResZh = localizer.t(barKey)
        
        // If it fails to find, it will return the key itself
        assertNotEquals("Chinese lookup for QR key should not fall back to key", qrKey, qrResZh)
        assertNotEquals("Chinese lookup for Barcode key should not fall back to key", barKey, barResZh)
        
        // Check that the returned translation contains real newlines, not literal \\n
        assertFalse("Chinese QR translation should not contain literal \\n text", qrResZh.contains("\\n"))
        assertTrue("Chinese QR translation should contain a real newline character", qrResZh.contains("\n"))
        
        assertFalse("Chinese Barcode translation should not contain literal \\n text", barResZh.contains("\\n"))
        assertTrue("Chinese Barcode translation should contain a real newline character", barResZh.contains("\n"))
    }
}