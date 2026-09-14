package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.data.models.TransformationOptions
import java.net.URI

class MediumTransformer(
    private val getScribeInstance: suspend () -> String = { "libmedium.batsense.net" }
) : LinkTransformer {

    override val supportedTypes = setOf(TransformationOptions.MEDIUM_SCRIBE.type)

    private val mediumHosts = setOf(
        "medium.com",
        "www.medium.com",
        "m.medium.com"
    )

    override fun canTransform(url: String): Boolean {
        return try {
            isMediumHost(URI(url))
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
        if (option.type != TransformationOptions.MEDIUM_SCRIBE.type) return url

        return try {
            val uri = URI(url)
            if (!isMediumHost(uri)) return url
            convertToScribe(uri)
        } catch (e: Exception) {
            url
        }
    }

    private suspend fun convertToScribe(originalUri: URI): String {
        return try {
            val uri = URI(TrackingCleaner.cleanUrl(originalUri.toString()))
            val path = uri.path ?: ""
            val query = uri.query?.let { "?$it" } ?: ""
            val fragment = uri.fragment?.let { "#$it" } ?: ""
            val scribeInstance = getScribeInstance()

            "https://$scribeInstance$path$query$fragment"
        } catch (e: Exception) {
            originalUri.toString()
        }
    }

    private fun isMediumHost(uri: URI): Boolean {
        return mediumHosts.contains(uri.host?.lowercase())
    }
}
