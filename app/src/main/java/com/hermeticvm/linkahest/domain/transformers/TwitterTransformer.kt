package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI

class TwitterTransformer(
    private val getNitterInstance: suspend () -> String
) : LinkTransformer {

    override val supportedTypes = setOf(TransformationOptions.TWITTER_NITTER.type)

    private val twitterHosts = setOf(
        "twitter.com",
        "www.twitter.com",
        "mobile.twitter.com",
        "x.com",
        "www.x.com"
    )
    
    override fun canTransform(url: String): Boolean {
        return try {
            isTwitterHost(URI(url))
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
        if (option.type != TransformationOptions.TWITTER_NITTER.type) return url

        return try {
            val uri = URI(url)
            if (!isTwitterHost(uri)) return url
            convertToNitter(uri)
        } catch (e: Exception) {
            url
        }
    }

    private suspend fun convertToNitter(uri: URI): String {
        return try {
            val path = uri.path ?: ""

            // Clean tracking parameters from query and fragment
            val cleanUri = URI(TrackingCleaner.cleanUrl(uri.toString()))

            val query = cleanUri.query?.let { "?$it" } ?: ""
            val fragment = cleanUri.fragment?.let { "#$it" } ?: ""

            val nitterInstance = getNitterInstance()
            "https://$nitterInstance$path$query$fragment"
        } catch (e: Exception) {
            uri.toString()
        }
    }

    private fun isTwitterHost(uri: URI): Boolean {
        return twitterHosts.contains(uri.host?.lowercase())
    }
}
