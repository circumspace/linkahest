package com.hermeticvm.linkahest.domain.usecases

import com.hermeticvm.linkahest.data.database.LinkTransformationDao
import com.hermeticvm.linkahest.data.models.LinkTransformation
import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.domain.transformers.MediumTransformer
import com.hermeticvm.linkahest.domain.transformers.RedditTransformer
import com.hermeticvm.linkahest.domain.transformers.TwitterTransformer
import com.hermeticvm.linkahest.domain.transformers.UniversalCleanerTransformer
import com.hermeticvm.linkahest.domain.transformers.YouTubeTransformer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TransformLinkUseCaseTest {
    @Test
    fun transformAndSaveSkipsHistoryWhenDisabled() = runTest {
        val dao = FakeLinkTransformationDao()
        val useCase = createUseCase(dao, shouldSaveHistory = false)

        val transformedUrl = useCase.transformAndSave(
            "https://youtu.be/dQw4w9WgXcQ?si=abc",
            TransformationOptions.YOUTUBE_INVIDIOUS
        )

        assertEquals("https://farside.link/invidious/watch?v=dQw4w9WgXcQ", transformedUrl)
        assertEquals(0, dao.getTransformationCount())
    }

    @Test
    fun transformAndSaveStoresHistoryWhenEnabled() = runTest {
        val dao = FakeLinkTransformationDao()
        val useCase = createUseCase(dao, shouldSaveHistory = true)

        useCase.transformAndSave(
            "https://youtu.be/dQw4w9WgXcQ?si=abc",
            TransformationOptions.YOUTUBE_INVIDIOUS
        )

        assertEquals(1, dao.getTransformationCount())
    }

    @Test
    fun reportsWhyUnsupportedYouTubeLinksCannotUseInvidious() {
        val useCase = createUseCase(FakeLinkTransformationDao(), shouldSaveHistory = false)
        val url = "https://youtube.com/@example/videos"

        assertFalse(
            useCase.getAvailableTransformations(url).contains(
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        )
        assertEquals(
            "This YouTube link type cannot currently be converted to Invidious. " +
                "Video, Shorts, Live, and embed links are supported.",
            useCase.getUnavailableReason(url)
        )
    }

    @Test
    fun transformUrlDispatchesByOptionType() = runTest {
        val useCase = createUseCase(FakeLinkTransformationDao(), shouldSaveHistory = false)

        assertEquals(
            "https://farside.link/invidious/watch?v=dQw4w9WgXcQ",
            useCase.transformUrl("https://youtu.be/dQw4w9WgXcQ", TransformationOptions.YOUTUBE_INVIDIOUS)
        )
        assertEquals(
            "https://farside.link/nitter/user/status/1",
            useCase.transformUrl("https://x.com/user/status/1", TransformationOptions.TWITTER_NITTER)
        )
        assertEquals(
            "https://farside.link/redlib/r/privacy",
            useCase.transformUrl("https://www.reddit.com/r/privacy", TransformationOptions.REDDIT_REDLIB)
        )
        assertEquals(
            "https://farside.link/scribe/@author/story",
            useCase.transformUrl("https://medium.com/@author/story", TransformationOptions.MEDIUM_SCRIBE)
        )
    }

    @Test
    fun transformUrlReturnsOriginalWhenOptionDoesNotMatchHost() = runTest {
        val useCase = createUseCase(FakeLinkTransformationDao(), shouldSaveHistory = false)
        val youtubeUrl = "https://youtu.be/dQw4w9WgXcQ"
        val redditUrl = "https://www.reddit.com/r/privacy"

        assertEquals(redditUrl, useCase.transformUrl(redditUrl, TransformationOptions.YOUTUBE_INVIDIOUS))
        assertEquals(youtubeUrl, useCase.transformUrl(youtubeUrl, TransformationOptions.TWITTER_NITTER))
        assertEquals(youtubeUrl, useCase.transformUrl(youtubeUrl, TransformationOptions.REDDIT_REDLIB))
        assertEquals(youtubeUrl, useCase.transformUrl(youtubeUrl, TransformationOptions.MEDIUM_SCRIBE))
    }

    @Test
    fun transformUrlIgnoresUnknownOptionType() = runTest {
        val useCase = createUseCase(FakeLinkTransformationDao(), shouldSaveHistory = false)
        val url = "https://youtu.be/dQw4w9WgXcQ"
        val unknown = TransformationOption(type = "does_not_exist", label = "", description = "")

        assertEquals(url, useCase.transformUrl(url, unknown))
    }

    @Test
    fun offersUniversalCleanerAlongsidePlatformOptionsForTrackedLinks() {
        val useCase = createUseCase(FakeLinkTransformationDao(), shouldSaveHistory = false)
        val types = useCase
            .getAvailableTransformations("https://youtu.be/dQw4w9WgXcQ?si=abc")
            .map { it.type }
            .toSet()

        assertEquals(setOf("universal_clean", TransformationOptions.YOUTUBE_INVIDIOUS.type), types)
    }

    private fun createUseCase(
        dao: LinkTransformationDao,
        shouldSaveHistory: Boolean
    ): TransformLinkUseCase {
        return TransformLinkUseCase(
            repository = LinkTransformationRepository(dao),
            youtubeTransformer = YouTubeTransformer { "farside.link/invidious" },
            twitterTransformer = TwitterTransformer { "farside.link/nitter" },
            redditTransformer = RedditTransformer { "farside.link/redlib" },
            mediumTransformer = MediumTransformer { "farside.link/scribe" },
            universalCleanerTransformer = UniversalCleanerTransformer(),
            shouldSaveHistory = { shouldSaveHistory }
        )
    }
}

private class FakeLinkTransformationDao : LinkTransformationDao {
    private val transformations = MutableStateFlow<List<LinkTransformation>>(emptyList())

    override fun getAllTransformations(): Flow<List<LinkTransformation>> {
        return transformations
    }

    override fun getTransformationsByType(type: String): Flow<List<LinkTransformation>> {
        return MutableStateFlow(transformations.value.filter { it.transformationType == type })
    }

    override suspend fun insertTransformation(transformation: LinkTransformation) {
        transformations.value = transformations.value + transformation
    }

    override suspend fun deleteTransformation(transformation: LinkTransformation) {
        transformations.value = transformations.value - transformation
    }

    override suspend fun deleteAllTransformations() {
        transformations.value = emptyList()
    }

    override suspend fun getTransformationCount(): Int {
        return transformations.value.size
    }
}
