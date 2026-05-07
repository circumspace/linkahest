package com.hermeticvm.linkahest

import android.app.Application
import kotlinx.coroutines.flow.first
import com.hermeticvm.linkahest.data.database.AppDatabase
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import com.hermeticvm.linkahest.domain.transformers.TwitterTransformer
import com.hermeticvm.linkahest.domain.transformers.YouTubeTransformer
import com.hermeticvm.linkahest.domain.transformers.RedditTransformer
import com.hermeticvm.linkahest.domain.transformers.MediumTransformer
import com.hermeticvm.linkahest.domain.transformers.UniversalCleanerTransformer
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
            val settings = settingsRepository.userSettings.first()
            if (settings.selectedNitterInstance == "custom") {
                settings.customNitterInstance.ifEmpty { "farside.link/nitter" }
            } else {
                settings.selectedNitterInstance
            }
        }
    }
    
    val youtubeTransformer by lazy { 
        YouTubeTransformer { 
            val settings = settingsRepository.userSettings.first()
            if (settings.selectedInvidiousInstance == "custom") {
                settings.customInvidiousInstance.ifEmpty { "farside.link/invidious" }
            } else {
                settings.selectedInvidiousInstance
            }
        }
    }
    
    val redditTransformer by lazy { 
        RedditTransformer { 
            val settings = settingsRepository.userSettings.first()
            if (settings.selectedRedlibInstance == "custom") {
                settings.customRedlibInstance.ifEmpty { "farside.link/redlib" }
            } else {
                settings.selectedRedlibInstance
            }
        }
    }
    
    val mediumTransformer by lazy {
        MediumTransformer {
            val settings = settingsRepository.userSettings.first()
            if (settings.selectedScribeInstance == "custom") {
                settings.customScribeInstance.ifEmpty { "farside.link/scribe" }
            } else {
                settings.selectedScribeInstance
            }
        }
    }

    val universalCleanerTransformer by lazy { UniversalCleanerTransformer() }
    
    // Use cases
    val transformLinkUseCase by lazy { 
        TransformLinkUseCase(
            repository,
            youtubeTransformer,
            twitterTransformer,
            redditTransformer,
            mediumTransformer,
            universalCleanerTransformer
        ) {
            settingsRepository.userSettings.first().historyEnabled
        }
    }
}
