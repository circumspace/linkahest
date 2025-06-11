package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.UserSettings
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
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
}

class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}