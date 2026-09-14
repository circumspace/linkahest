package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TransformerDispatchTest {

    private val youtube = YouTubeTransformer { "inv.example" }
    private val twitter = TwitterTransformer { "nitter.example" }
    private val reddit = RedditTransformer { "redlib.example" }
    private val medium = MediumTransformer { "scribe.example" }
    private val universal = UniversalCleanerTransformer()

    private val youtubeUrl = "https://youtu.be/dQw4w9WgXcQ"
    private val twitterUrl = "https://x.com/user/status/123"
    private val redditUrl = "https://www.reddit.com/r/privacy"
    private val mediumUrl = "https://medium.com/@author/story"
    private val trackedUrl = "https://example.com/article?utm_source=newsletter"

    @Test
    fun declaredSupportedTypesMatchOfferedOptions() {
        assertDeclaredTypesMatch(youtube, youtubeUrl)
        assertDeclaredTypesMatch(twitter, twitterUrl)
        assertDeclaredTypesMatch(reddit, redditUrl)
        assertDeclaredTypesMatch(medium, mediumUrl)
        assertDeclaredTypesMatch(universal, trackedUrl)
    }

    @Test
    fun eachTransformerOffersExactlyOneType() {
        assertEquals(setOf(TransformationOptions.YOUTUBE_INVIDIOUS.type), youtube.supportedTypes)
        assertEquals(setOf(TransformationOptions.TWITTER_NITTER.type), twitter.supportedTypes)
        assertEquals(setOf(TransformationOptions.REDDIT_REDLIB.type), reddit.supportedTypes)
        assertEquals(setOf(TransformationOptions.MEDIUM_SCRIBE.type), medium.supportedTypes)
        assertEquals(setOf("universal_clean"), universal.supportedTypes)
    }

    @Test
    fun rejectsUrlsThatDoNotBelongToTheTransformer() = runTest {
        assertEquals(redditUrl, youtube.transform(redditUrl, TransformationOptions.YOUTUBE_INVIDIOUS))
        assertEquals(youtubeUrl, twitter.transform(youtubeUrl, TransformationOptions.TWITTER_NITTER))
        assertEquals(youtubeUrl, reddit.transform(youtubeUrl, TransformationOptions.REDDIT_REDLIB))
        assertEquals(youtubeUrl, medium.transform(youtubeUrl, TransformationOptions.MEDIUM_SCRIBE))
    }

    @Test
    fun ignoresOptionTypesItDoesNotSupport() = runTest {
        assertEquals(youtubeUrl, youtube.transform(youtubeUrl, TransformationOptions.TWITTER_NITTER))
        assertEquals(twitterUrl, twitter.transform(twitterUrl, TransformationOptions.REDDIT_REDLIB))
        assertEquals(redditUrl, reddit.transform(redditUrl, TransformationOptions.MEDIUM_SCRIBE))
        assertEquals(mediumUrl, medium.transform(mediumUrl, TransformationOptions.YOUTUBE_INVIDIOUS))
    }

    @Test
    fun ignoresUnknownOptionType() = runTest {
        val unknown = TransformationOption(type = "does_not_exist", label = "", description = "")

        assertEquals(youtubeUrl, youtube.transform(youtubeUrl, unknown))
        assertEquals(twitterUrl, twitter.transform(twitterUrl, unknown))
        assertEquals(redditUrl, reddit.transform(redditUrl, unknown))
        assertEquals(mediumUrl, medium.transform(mediumUrl, unknown))
        assertEquals(trackedUrl, universal.transform(trackedUrl, unknown))
    }

    @Test
    fun universalCleanerDropsTrackingButKeepsCleanUrls() = runTest {
        val cleanOption = TransformationOption("universal_clean", "", "")

        assertEquals("https://example.com/article", universal.transform(trackedUrl, cleanOption))
        assertEquals(
            "https://example.com/article?id=1",
            universal.transform("https://example.com/article?id=1", cleanOption)
        )
    }

    private fun assertDeclaredTypesMatch(transformer: LinkTransformer, matchingUrl: String) {
        val offeredTypes = transformer.getTransformationOptions(matchingUrl).map { it.type }.toSet()
        assertEquals(transformer.supportedTypes, offeredTypes)
    }
}
