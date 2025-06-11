package com.hermeticvm.linkahest.domain.usecases

import com.hermeticvm.linkahest.data.models.LinkTransformation
import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.domain.transformers.LinkTransformer
import com.hermeticvm.linkahest.domain.transformers.YouTubeTransformer
import com.hermeticvm.linkahest.domain.transformers.TwitterTransformer
import com.hermeticvm.linkahest.domain.transformers.RedditTransformer

class TransformLinkUseCase(
    private val repository: LinkTransformationRepository,
    private val youtubeTransformer: YouTubeTransformer,
    private val twitterTransformer: TwitterTransformer,
    private val redditTransformer: RedditTransformer
) {
    
    private val transformers: List<LinkTransformer> = listOf(
        youtubeTransformer,
        twitterTransformer,
        redditTransformer
    )
    
    fun getAvailableTransformations(url: String): List<TransformationOption> {
        return transformers.flatMap { transformer ->
            transformer.getTransformationOptions(url)
        }
    }
    
    suspend fun transformUrl(url: String, option: TransformationOption): String {
        return transformers.firstOrNull { transformer ->
            transformer.canTransform(url)
        }?.transform(url, option) ?: url
    }
    
    suspend fun transformAndSave(url: String, option: TransformationOption): String {
        val transformedUrl = transformUrl(url, option)
        
        if (transformedUrl != url) {
            val transformation = LinkTransformation(
                originalUrl = url,
                transformedUrl = transformedUrl,
                transformationType = option.type
            )
            repository.saveTransformation(transformation)
        }
        
        return transformedUrl
    }
    
    fun canTransform(url: String): Boolean {
        return transformers.any { it.canTransform(url) }
    }
}