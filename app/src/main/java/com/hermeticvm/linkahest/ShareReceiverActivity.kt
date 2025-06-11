package com.hermeticvm.linkahest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hermeticvm.linkahest.ui.theme.LinkahestTheme
import com.hermeticvm.linkahest.ui.screens.TransformationScreen
import com.hermeticvm.linkahest.ui.screens.TransformationViewModel

class ShareReceiverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sharedText = when {
            intent?.action == Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    intent.getStringExtra(Intent.EXTRA_TEXT)
                } else null
            }
            else -> null
        }
        
        if (sharedText != null) {
            setContent {
                LinkahestTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val application = application as LinkahestApplication
                        val viewModel: TransformationViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                    return TransformationViewModel(application.transformLinkUseCase) as T
                                }
                            }
                        )
                        
                        TransformationScreen(
                            originalUrl = sharedText,
                            viewModel = viewModel,
                            onNavigateBack = { finish() }
                        )
                    }
                }
            }
        } else {
            // No valid shared content, close the activity
            finish()
        }
    }
}