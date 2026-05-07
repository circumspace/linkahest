package com.hermeticvm.linkahest.domain.usecases

import com.hermeticvm.linkahest.data.database.LinkTransformationDao
import com.hermeticvm.linkahest.data.models.LinkTransformation
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
import org.junit.Test

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
