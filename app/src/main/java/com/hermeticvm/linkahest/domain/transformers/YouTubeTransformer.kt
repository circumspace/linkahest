package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI
import java.net.URLDecoder

class YouTubeTransformer(
    private val getInvidiousInstance: suspend () -> String
) : LinkTransformer {

    override val supportedTypes = setOf(TransformationOptions.YOUTUBE_INVIDIOUS.type)

    private val youtubeHosts = setOf(
        "youtube.com",
        "www.youtube.com",
        "m.youtube.com",
        "music.youtube.com",
        "youtube-nocookie.com",
        "www.youtube-nocookie.com",
        "youtu.be"
    )
    
    override fun canTransform(url: String): Boolean {
        return try {
            val uri = URI(url)
            isYouTubeHost(uri) && extractVideoId(uri) != null
        } catch (e: Exception) {
            false
        }
    }

    override fun getUnavailableReason(url: String): String? {
        return try {
            val uri = URI(url)
            if (isYouTubeHost(uri) && extractVideoId(uri) == null) {
                "This YouTube link type cannot currently be converted to Invidious. " +
                    "Video, Shorts, Live, and embed links are supported."
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override fun getTransformationOptions(url: String): List<TransformationOption> {
        return if (canTransform(url)) {
            listOf(TransformationOptions.YOUTUBE_INVIDIOUS)
        } else {
            emptyList()
        }
    }
    
    override suspend fun transform(url: String, option: TransformationOption): String {
        if (option.type != TransformationOptions.YOUTUBE_INVIDIOUS.type) return url

        return try {
            val uri = URI(url)
            if (!isYouTubeHost(uri)) return url

            val cleanUri = URI(TrackingCleaner.cleanUrl(url))
            val videoId = extractVideoId(cleanUri) ?: return url
            "https://${getInvidiousInstance()}/watch?v=$videoId"
        } catch (e: Exception) {
            url
        }
    }

    private fun isYouTubeHost(uri: URI): Boolean {
        return uri.host?.lowercase() in youtubeHosts
    }
    
    private fun extractVideoId(uri: URI): String? {
        return when {
            uri.host?.equals("youtu.be", ignoreCase = true) == true -> {
                // youtu.be format: https://youtu.be/VIDEO_ID
                extractPathSegment(uri.path, "/")
            }
            uri.path == "/watch" -> {
                // youtube.com/watch format
                uri.query?.split("&")
                    ?.find { it.startsWith("v=") }
                    ?.substringAfter("v=")
                    ?.let { URLDecoder.decode(it, "UTF-8") }
            }
            uri.path?.startsWith("/embed/") == true -> {
                // youtube.com/embed format
                extractPathSegment(uri.path, "/embed/")
            }
            uri.path?.startsWith("/shorts/") == true -> {
                extractPathSegment(uri.path, "/shorts/")
            }
            uri.path?.startsWith("/live/") == true -> {
                extractPathSegment(uri.path, "/live/")
            }
            uri.path?.startsWith("/v/") == true -> {
                extractPathSegment(uri.path, "/v/")
            }
            else -> null
        }
    }

    private fun extractPathSegment(path: String?, prefix: String): String? {
        return path
            ?.removePrefix(prefix)
            ?.substringBefore('/')
            ?.takeIf { it.isNotBlank() }
    }
}
