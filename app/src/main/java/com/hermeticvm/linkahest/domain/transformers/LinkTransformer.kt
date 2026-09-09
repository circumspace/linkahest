package com.hermeticvm.linkahest.domain.transformers

import com.hermeticvm.linkahest.data.models.TransformationOption

interface LinkTransformer {
    fun canTransform(url: String): Boolean
    fun getTransformationOptions(url: String): List<TransformationOption>
    fun getUnavailableReason(url: String): String? = null
    suspend fun transform(url: String, option: TransformationOption): String
}
