package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption

class UniversalCleanerTransformer : LinkTransformer {
    
    override fun canTransform(url: String): Boolean {
        return TrackingCleaner.hasTrackingParameters(url)
    }
    
    override fun getTransformationOptions(url: String): List<TransformationOption> {
        if (!canTransform(url)) return emptyList()
        
        return listOf(
            TransformationOption(
                type = "universal_clean",
                label = "Remove Tracking Parameters",
                description = "Remove UTM codes, tracking IDs, and other tracking parameters"
            )
        )
    }
    
    override suspend fun transform(url: String, option: TransformationOption): String {
        return when (option.type) {
            "universal_clean" -> TrackingCleaner.cleanUrl(url)
            else -> url
        }
    }
}
