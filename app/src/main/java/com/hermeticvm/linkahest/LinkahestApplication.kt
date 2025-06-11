package com.hermeticvm.linkahest

import android.app.Application
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.hermeticvm.linkahest.data.database.AppDatabase
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import com.hermeticvm.linkahest.domain.transformers.TwitterTransformer
import com.hermeticvm.linkahest.domain.transformers.YouTubeTransformer
import com.hermeticvm.linkahest.domain.transformers.RedditTransformer
import com.hermeticvm.linkahest.domain.usecases.TransformLinkUseCase

class LinkahestApplication : Application() {
    
    // Database
    val database by lazy { AppDatabase.getDatabase(this) }
    
    // Repositories
    val repository by lazy { LinkTransformationRepository(database.linkTransformationDao()) }
    val settingsRepository by lazy { SettingsRepository(this) }
    
    // Transformers with settings dependency
    val twitterTransformer by lazy { 
        TwitterTransformer { 
            runBlocking {
                val settings = settingsRepository.userSettings.first()
                if (settings.selectedNitterInstance == "custom") {
                    settings.customNitterInstance.ifEmpty { "nitter.net" }
                } else {
                    settings.selectedNitterInstance
                }
            }
        }
    }
    
    val youtubeTransformer by lazy { 
        YouTubeTransformer { 
            runBlocking {
                val settings = settingsRepository.userSettings.first()
                if (settings.selectedInvidiousInstance == "custom") {
                    settings.customInvidiousInstance.ifEmpty { "inv.nadeko.net" }
                } else {
                    settings.selectedInvidiousInstance
                }
            }
        }
    }
    
    val redditTransformer by lazy { 
        RedditTransformer { 
            runBlocking {
                val settings = settingsRepository.userSettings.first()
                if (settings.selectedRedlibInstance == "custom") {
                    settings.customRedlibInstance.ifEmpty { "rl.bloat.cat" }
                } else {
                    settings.selectedRedlibInstance
                }
            }
        }
    }
    
    // Use cases
    val transformLinkUseCase by lazy { 
        TransformLinkUseCase(repository, youtubeTransformer, twitterTransformer, redditTransformer) 
    }
}