package com.hermeticvm.linkahest.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "link_transformations")
data class LinkTransformation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originalUrl: String,
    val transformedUrl: String,
    val transformationType: String, // "youtube_clean", "youtube_invidious", "twitter_nitter"
    val timestamp: Date = Date()
)