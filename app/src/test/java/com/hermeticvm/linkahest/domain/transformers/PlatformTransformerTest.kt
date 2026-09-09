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
    fun convertsFullYouTubeWatchLinkToConfiguredInvidiousInstance() = runTest {
        val transformer = YouTubeTransformer { "redirect.invidious.io" }

        assertEquals(
            "https://redirect.invidious.io/watch?v=dQw4w9WgXcQ",
            transformer.transform(
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ&si=abc",
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
    }

    @Test
    fun convertsYouTubeShortToConfiguredInvidiousInstance() = runTest {
        val transformer = YouTubeTransformer { "redirect.invidious.io" }

        assertEquals(
            "https://redirect.invidious.io/watch?v=dQw4w9WgXcQ",
            transformer.transform(
                "https://www.youtube.com/shorts/dQw4w9WgXcQ?si=abc",
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
    }

    @Test
    fun convertsYouTubeLiveLinkToConfiguredInvidiousInstance() = runTest {
        val transformer = YouTubeTransformer { "redirect.invidious.io" }

        assertEquals(
            "https://redirect.invidious.io/watch?v=dQw4w9WgXcQ",
            transformer.transform(
                "https://youtube.com/live/dQw4w9WgXcQ?si=abc",
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
    }

    @Test
    fun convertsYouTubeEmbedToConfiguredInvidiousInstance() = runTest {
        val transformer = YouTubeTransformer { "redirect.invidious.io" }

        assertEquals(
            "https://redirect.invidious.io/watch?v=dQw4w9WgXcQ",
            transformer.transform(
                "https://www.youtube-nocookie.com/embed/dQw4w9WgXcQ",
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
    }

    @Test
    fun explainsUnsupportedYouTubePathsInsteadOfOfferingConversion() {
        val transformer = YouTubeTransformer { "redirect.invidious.io" }

        val url = "https://youtube.com/@example/videos?feature=shared"

        assertTrue(transformer.getTransformationOptions(url).isEmpty())
        assertEquals(
            "This YouTube link type cannot currently be converted to Invidious. " +
                "Video, Shorts, Live, and embed links are supported.",
            transformer.getUnavailableReason(url)
        )
    }

    @Test
    fun convertsYouTubeMusicLinks() = runTest {
        val transformer = YouTubeTransformer { "redirect.invidious.io" }

        assertEquals(
            "https://redirect.invidious.io/watch?v=dQw4w9WgXcQ",
            transformer.transform(
                "https://music.youtube.com/watch?v=dQw4w9WgXcQ&feature=share",
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
    }

    @Test
    fun convertsTwitterToConfiguredNitterInstanceAndRemovesTracking() = runTest {
        val transformer = TwitterTransformer { "nitter.click" }

        assertEquals(
            "https://nitter.click/user/status/123",
            transformer.transform(
                "https://x.com/user/status/123?s=20&t=abc",
                TransformationOptions.TWITTER_NITTER
            )
        )
    }

    @Test
    fun convertsTwitterToRedirectorAndPreservesStatusPath() = runTest {
        val transformer = TwitterTransformer { "xxcancel.com" }

        assertEquals(
            "https://xxcancel.com/jack/status/20",
            transformer.transform(
                "https://x.com/jack/status/20",
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
