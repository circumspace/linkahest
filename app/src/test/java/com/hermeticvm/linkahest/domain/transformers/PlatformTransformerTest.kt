package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOptions
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlatformTransformerTest {
    @Test
    fun youtubeOnlyOffersInvidiousConversion() {
        val transformer = YouTubeTransformer { "farside.link/invidious" }
        val options = transformer.getTransformationOptions("https://www.youtube.com/watch?v=dQw4w9WgXcQ&si=abc")

        assertEquals(listOf(TransformationOptions.YOUTUBE_INVIDIOUS), options)
    }

    @Test
    fun convertsYouTubeToConfiguredInvidiousInstance() = runTest {
        val transformer = YouTubeTransformer { "farside.link/invidious" }

        assertEquals(
            "https://farside.link/invidious/watch?v=dQw4w9WgXcQ",
            transformer.transform(
                "https://youtu.be/dQw4w9WgXcQ?si=abc",
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
    }

    @Test
    fun convertsTwitterToConfiguredNitterInstanceAndRemovesTracking() = runTest {
        val transformer = TwitterTransformer { "farside.link/nitter" }

        assertEquals(
            "https://farside.link/nitter/user/status/123",
            transformer.transform(
                "https://x.com/user/status/123?s=20&t=abc",
                TransformationOptions.TWITTER_NITTER
            )
        )
    }

    @Test
    fun convertsRedditToConfiguredRedlibInstanceAndKeepsUsefulQuery() = runTest {
        val transformer = RedditTransformer { "farside.link/redlib" }

        assertEquals(
            "https://farside.link/redlib/r/privacy/comments/abc?context=3",
            transformer.transform(
                "https://www.reddit.com/r/privacy/comments/abc?utm_source=share&context=3",
                TransformationOptions.REDDIT_REDLIB
            )
        )
    }

    @Test
    fun convertsMediumToConfiguredScribeInstanceAndRemovesTracking() = runTest {
        val transformer = MediumTransformer { "farside.link/scribe" }

        assertEquals(
            "https://farside.link/scribe/@ftrain/big-data-small-effort-b62607a43a8c",
            transformer.transform(
                "https://medium.com/@ftrain/big-data-small-effort-b62607a43a8c?source=share&utm_source=email",
                TransformationOptions.MEDIUM_SCRIBE
            )
        )
    }

    @Test
    fun mediumOnlyOffersScribeConversion() {
        val transformer = MediumTransformer { "farside.link/scribe" }
        val options = transformer.getTransformationOptions("https://medium.com/@ftrain/story")

        assertEquals(listOf(TransformationOptions.MEDIUM_SCRIBE), options)
    }

    @Test
    fun universalCleanerOnlyAppearsWhenTrackingExists() {
        val transformer = UniversalCleanerTransformer()

        assertTrue(transformer.getTransformationOptions("https://example.com/?utm_source=x").isNotEmpty())
        assertFalse(transformer.getTransformationOptions("https://example.com/?id=x").isNotEmpty())
    }
}
