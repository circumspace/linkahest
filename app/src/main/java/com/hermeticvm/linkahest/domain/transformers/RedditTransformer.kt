package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI

class RedditTransformer(
    private val getRedlibInstance: suspend () -> String
) : LinkTransformer {

    override val supportedTypes = setOf(TransformationOptions.REDDIT_REDLIB.type)

    private val redditHosts = setOf(
        "reddit.com",
        "www.reddit.com",
        "old.reddit.com",
        "new.reddit.com",
        "m.reddit.com",
        "mobile.reddit.com"
    )
    
    override fun canTransform(url: String): Boolean {
        return try {
            isRedditHost(URI(url))
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getTransformationOptions(url: String): List<TransformationOption> {
        return if (canTransform(url)) {
            listOf(TransformationOptions.REDDIT_REDLIB)
        } else {
            emptyList()
        }
    }
    
    override suspend fun transform(url: String, option: TransformationOption): String {
        if (option.type != TransformationOptions.REDDIT_REDLIB.type) return url

        return try {
            val uri = URI(url)
            if (!isRedditHost(uri)) return url
            convertToRedlib(uri)
        } catch (e: Exception) {
            url
        }
    }

    private suspend fun convertToRedlib(uri: URI): String {
        return try {
            val path = uri.path ?: ""

            // Clean tracking parameters from query and fragment
            val cleanUri = URI(TrackingCleaner.cleanUrl(uri.toString()))

            val query = cleanUri.query?.let { "?$it" } ?: ""
            val fragment = cleanUri.fragment?.let { "#$it" } ?: ""

            val redlibInstance = getRedlibInstance()
            "https://$redlibInstance$path$query$fragment"
        } catch (e: Exception) {
            uri.toString()
        }
    }

    private fun isRedditHost(uri: URI): Boolean {
        return redditHosts.contains(uri.host?.lowercase())
    }
}
