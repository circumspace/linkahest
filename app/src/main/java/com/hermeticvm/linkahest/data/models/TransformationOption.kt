package com.hermeticvm.linkahest.data.models

data class TransformationOption(
    val type: String,
    val label: String,
    val description: String
)

// Predefined transformation options
object TransformationOptions {
    val YOUTUBE_CLEAN = TransformationOption(
        type = "youtube_clean",
        label = "Clean YouTube Link",
        description = "Remove tracking parameters"
    )
    
    val YOUTUBE_INVIDIOUS = TransformationOption(
        type = "youtube_invidious", 
        label = "YouTube to Invidious",
        description = "Convert to privacy-focused Invidious instance"
    )
    
    val TWITTER_NITTER = TransformationOption(
        type = "twitter_nitter",
        label = "Twitter to Nitter", 
        description = "Convert to privacy-focused Nitter instance"
    )
    
    val REDDIT_REDLIB = TransformationOption(
        type = "reddit_redlib",
        label = "Reddit to Redlib",
        description = "Convert to privacy-focused Redlib instance"
    )
}