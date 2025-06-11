package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI
import java.net.URLDecoder

class YouTubeTransformer(
    private val getInvidiousInstance: suspend () -> String
) : LinkTransformer {
    
    private val youtubeHosts = setOf(
        "youtube.com",
        "www.youtube.com", 
        "m.youtube.com",
        "youtu.be"
    )
    
    override fun canTransform(url: String): Boolean {
        return try {
            val uri = URI(url)
            youtubeHosts.contains(uri.host?.lowercase())
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getTransformationOptions(url: String): List<TransformationOption> {
        return if (canTransform(url)) {
            listOf(
                TransformationOptions.YOUTUBE_CLEAN,
                TransformationOptions.YOUTUBE_INVIDIOUS
            )
        } else {
            emptyList()
        }
    }
    
    override suspend fun transform(url: String, option: TransformationOption): String {
        if (!canTransform(url)) return url
        
        return when (option.type) {
            "youtube_clean" -> cleanYouTubeUrl(url)
            "youtube_invidious" -> convertToInvidious(url)
            else -> url
        }
    }
    
    private fun cleanYouTubeUrl(url: String): String {
        try {
            val uri = URI(url)
            val videoId = extractVideoId(uri) ?: return url
            
            // Return clean YouTube URL
            return "https://www.youtube.com/watch?v=$videoId"
        } catch (e: Exception) {
            return url
        }
    }
    
    private suspend fun convertToInvidious(url: String): String {
        try {
            val uri = URI(url)
            val videoId = extractVideoId(uri) ?: return url
            
            // Convert to Invidious
            val invidiousInstance = getInvidiousInstance()
            return "https://$invidiousInstance/watch?v=$videoId"
        } catch (e: Exception) {
            return url
        }
    }
    
    private fun extractVideoId(uri: URI): String? {
        return when {
            uri.host?.contains("youtu.be") == true -> {
                // youtu.be format: https://youtu.be/VIDEO_ID
                uri.path?.removePrefix("/")
            }
            uri.path?.startsWith("/watch") == true -> {
                // youtube.com/watch format
                uri.query?.split("&")
                    ?.find { it.startsWith("v=") }
                    ?.substringAfter("v=")
                    ?.let { URLDecoder.decode(it, "UTF-8") }
            }
            uri.path?.startsWith("/embed/") == true -> {
                // youtube.com/embed format
                uri.path?.removePrefix("/embed/")
            }
            else -> null
        }
    }
}