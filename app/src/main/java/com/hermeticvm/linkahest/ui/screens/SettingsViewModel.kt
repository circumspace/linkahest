package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.UserSettings
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val historyRepository: LinkTransformationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserSettings())
    val uiState: StateFlow<UserSettings> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.userSettings.collect { settings ->
                _uiState.value = settings
            }
        }
    }
    
    fun selectNitterInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateNitterInstance(instance)
        }
    }
    
    fun updateCustomNitterInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateCustomNitterInstance(instance)
        }
    }
    
    fun selectInvidiousInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateInvidiousInstance(instance)
        }
    }
    
    fun updateCustomInvidiousInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateCustomInvidiousInstance(instance)
        }
    }
    
    fun selectRedlibInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateRedlibInstance(instance)
        }
    }
    
    fun updateCustomRedlibInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateCustomRedlibInstance(instance)
        }
    }

    fun selectScribeInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateScribeInstance(instance)
        }
    }

    fun updateCustomScribeInstance(instance: String) {
        viewModelScope.launch {
            settingsRepository.updateCustomScribeInstance(instance)
        }
    }

    fun updateThemeMode(themeMode: String) {
        viewModelScope.launch {
            settingsRepository.updateThemeMode(themeMode)
        }
    }

    fun updateHistoryEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateHistoryEnabled(enabled)
            if (!enabled) {
                historyRepository.clearAllTransformations()
            }
        }
    }

}

fun normalizeInstance(instance: String): String {
    return instance
        .trim()
        .removePrefix("https://")
        .removePrefix("http://")
        .trimEnd('/')
}

fun isRedirectingInstance(instance: String): Boolean {
    val normalized = normalizeInstance(instance)
    return normalized.startsWith("farside.link/") ||
        normalized == "twiiit.com" ||
        normalized == "redirect.invidious.io"
}

class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val historyRepository: LinkTransformationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository, historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
