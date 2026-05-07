package com.hermeticvm.linkahest.domain.usecases

import com.hermeticvm.linkahest.data.models.LinkTransformation
import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.domain.transformers.LinkTransformer
import com.hermeticvm.linkahest.domain.transformers.YouTubeTransformer
import com.hermeticvm.linkahest.domain.transformers.TwitterTransformer
import com.hermeticvm.linkahest.domain.transformers.RedditTransformer
import com.hermeticvm.linkahest.domain.transformers.MediumTransformer
import com.hermeticvm.linkahest.domain.transformers.UniversalCleanerTransformer

class TransformLinkUseCase(
    private val repository: LinkTransformationRepository,
    private val youtubeTransformer: YouTubeTransformer,
    private val twitterTransformer: TwitterTransformer,
    private val redditTransformer: RedditTransformer,
    private val mediumTransformer: MediumTransformer,
    private val universalCleanerTransformer: UniversalCleanerTransformer,
    private val shouldSaveHistory: suspend () -> Boolean = { true }
) {
    
    private val transformers: List<LinkTransformer> = listOf(
        youtubeTransformer,
        twitterTransformer,
        redditTransformer,
        mediumTransformer,
        universalCleanerTransformer
    )
    
    fun getAvailableTransformations(url: String): List<TransformationOption> {
        val options = mutableListOf<TransformationOption>()
        
        // First, check if universal cleaning is available
        val universalOptions = universalCleanerTransformer.getTransformationOptions(url)
        if (universalOptions.isNotEmpty()) {
            options.addAll(universalOptions)
        }
        
        // Then add platform-specific transformations
        transformers.filter { it !is UniversalCleanerTransformer }
            .forEach { transformer ->
                options.addAll(transformer.getTransformationOptions(url))
            }
        
        return options
    }
    
    suspend fun transformUrl(url: String, option: TransformationOption): String {
        val transformer = transformers.firstOrNull { transformer ->
            transformer.getTransformationOptions(url).any { it.type == option.type }
        }

        return transformer?.transform(url, option) ?: url
    }
    
    suspend fun transformAndSave(url: String, option: TransformationOption): String {
        val transformedUrl = transformUrl(url, option)
        
        if (transformedUrl != url && shouldSaveHistory()) {
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
