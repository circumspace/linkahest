package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI

class MediumTransformer(
    private val getScribeInstance: suspend () -> String = { "libmedium.batsense.net" }
) : LinkTransformer {

    private val mediumHosts = setOf(
        "medium.com",
        "www.medium.com",
        "m.medium.com"
    )

    override fun canTransform(url: String): Boolean {
        return try {
            val uri = URI(url)
            mediumHosts.contains(uri.host?.lowercase())
        } catch (e: Exception) {
            false
        }
    }

    override fun getTransformationOptions(url: String): List<TransformationOption> {
        return if (canTransform(url)) {
            listOf(TransformationOptions.MEDIUM_SCRIBE)
        } else {
            emptyList()
        }
    }

    override suspend fun transform(url: String, option: TransformationOption): String {
        if (!canTransform(url)) return url

        return when (option.type) {
            "medium_scribe" -> convertToScribe(url)
            else -> url
        }
    }

    private suspend fun convertToScribe(url: String): String {
        return try {
            val cleanUrl = TrackingCleaner.cleanUrl(url)
            val uri = URI(cleanUrl)
            val path = uri.path ?: ""
            val query = uri.query?.let { "?$it" } ?: ""
            val fragment = uri.fragment?.let { "#$it" } ?: ""
            val scribeInstance = getScribeInstance()

            "https://$scribeInstance$path$query$fragment"
        } catch (e: Exception) {
            url
        }
    }
}
