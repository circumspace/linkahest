package com.hermeticvm.linkahest.domain.transformers

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackingCleanerTest {
    @Test
    fun removesCommonTrackingParameters() {
        val url = "https://example.com/article?utm_source=newsletter&id=42&fbclid=abc"

        assertEquals(
            "https://example.com/article?id=42",
            TrackingCleaner.cleanUrl(url)
        )
    }

    @Test
    fun detectsTrackingParameters() {
        assertTrue(TrackingCleaner.hasTrackingParameters("https://example.com/?gclid=abc"))
        assertFalse(TrackingCleaner.hasTrackingParameters("https://example.com/?id=abc"))
    }

    @Test
    fun extractsAndCleansCloakedUrls() {
        val url = "https://redirect.example.com/out/https://example.com/page?utm_medium=social&id=7"

        assertEquals(
            "https://example.com/page?id=7",
            TrackingCleaner.cleanUrl(url)
        )
    }
}
