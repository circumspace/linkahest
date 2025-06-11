package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI

class RedditTransformer(
    private val getRedlibInstance: suspend () -> String
) : LinkTransformer {
    
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
            val uri = URI(url)
            redditHosts.contains(uri.host?.lowercase())
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
        if (!canTransform(url)) return url
        
        return when (option.type) {
            "reddit_redlib" -> convertToRedlib(url)
            else -> url
        }
    }
    
    private suspend fun convertToRedlib(url: String): String {
        try {
            val uri = URI(url)
            val path = uri.path ?: ""
            val query = uri.query?.let { "?$it" } ?: ""
            val fragment = uri.fragment?.let { "#$it" } ?: ""
            
            val redlibInstance = getRedlibInstance()
            return "https://$redlibInstance$path$query$fragment"
        } catch (e: Exception) {
            return url
        }
    }
}