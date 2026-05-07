package com.hermeticvm.linkahest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.ui.screens.HistoryScreen
import com.hermeticvm.linkahest.ui.screens.HistoryViewModel
import com.hermeticvm.linkahest.ui.screens.HistoryViewModelFactory
import com.hermeticvm.linkahest.ui.screens.SettingsScreen
import com.hermeticvm.linkahest.ui.screens.SettingsViewModel
import com.hermeticvm.linkahest.ui.screens.SettingsViewModelFactory
import com.hermeticvm.linkahest.ui.theme.LinkahestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val application = application as LinkahestApplication
            val settings by application.settingsRepository.userSettings.collectAsState(initial = null)

            LinkahestTheme(themeMode = settings?.themeMode ?: "system") {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as LinkahestApplication
    val historyRepository: LinkTransformationRepository = application.repository
    val settingsRepository = application.settingsRepository
    val settings by settingsRepository.userSettings.collectAsState(initial = null)

    LaunchedEffect(settings?.historyEnabled) {
        if (settings?.historyEnabled == false) {
            historyRepository.clearAllTransformations()
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                historyEnabled = settings?.historyEnabled ?: true,
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToHistory = {
                    navController.navigate("history")
                },
                onNavigateToHistorySettings = {
                    navController.navigate("settings")
                }
            )
        }
        composable("settings") {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(settingsRepository, historyRepository)
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("history") {
            val historyViewModel: HistoryViewModel = viewModel(
                factory = HistoryViewModelFactory(historyRepository, settingsRepository)
            )
            HistoryScreen(
                viewModel = historyViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
