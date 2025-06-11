package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI

class TwitterTransformer(
    private val getNitterInstance: suspend () -> String
) : LinkTransformer {
    
    private val twitterHosts = setOf(
        "twitter.com",
        "www.twitter.com",
        "mobile.twitter.com",
        "x.com",
        "www.x.com"
    )
    
    override fun canTransform(url: String): Boolean {
        return try {
            val uri = URI(url)
            twitterHosts.contains(uri.host?.lowercase())
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getTransformationOptions(url: String): List<TransformationOption> {
        return if (canTransform(url)) {
            listOf(TransformationOptions.TWITTER_NITTER)
        } else {
            emptyList()
        }
    }
    
    override suspend fun transform(url: String, option: TransformationOption): String {
        if (!canTransform(url)) return url
        
        return when (option.type) {
            "twitter_nitter" -> convertToNitter(url)
            else -> url
        }
    }
    
    private suspend fun convertToNitter(url: String): String {
        try {
            val uri = URI(url)
            val path = uri.path ?: ""
            val query = uri.query?.let { "?$it" } ?: ""
            val fragment = uri.fragment?.let { "#$it" } ?: ""
            
            val nitterInstance = getNitterInstance()
            return "https://$nitterInstance$path$query$fragment"
        } catch (e: Exception) {
            return url
        }
    }
}