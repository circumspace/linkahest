package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.UserSettings
import com.hermeticvm.linkahest.data.models.DefaultInstances
import com.hermeticvm.linkahest.data.repository.InstanceAvailabilityRepository
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val historyRepository: LinkTransformationRepository,
    private val availabilityRepository: InstanceAvailabilityRepository = InstanceAvailabilityRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserSettings())
    val uiState: StateFlow<UserSettings> = _uiState.asStateFlow()
    private val _instanceAvailability = MutableStateFlow<Map<String, InstanceAvailability>>(emptyMap())
    val instanceAvailability: StateFlow<Map<String, InstanceAvailability>> =
        _instanceAvailability.asStateFlow()
    private val _isCheckingInstances = MutableStateFlow(false)
    val isCheckingInstances: StateFlow<Boolean> = _isCheckingInstances.asStateFlow()

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

    fun checkInstances() {
        if (_isCheckingInstances.value) return

        viewModelScope.launch {
            val settings = _uiState.value
            val targets = (
                DefaultInstances.NITTER_INSTANCES.map { InstanceProbeTarget(it, "/jack") } +
                    DefaultInstances.INVIDIOUS_INSTANCES.map {
                        InstanceProbeTarget(it, "/watch?v=dQw4w9WgXcQ")
                    } +
                    DefaultInstances.REDLIB_INSTANCES.map { InstanceProbeTarget(it, "/r/privacy") } +
                    DefaultInstances.SCRIBE_INSTANCES.map {
                        InstanceProbeTarget(it, "/@ftrain/big-data-small-effort-b62607a43a8c")
                    } +
                    listOf(
                        InstanceProbeTarget(settings.customNitterInstance, "/jack"),
                        InstanceProbeTarget(
                            settings.customInvidiousInstance,
                            "/watch?v=dQw4w9WgXcQ"
                        ),
                        InstanceProbeTarget(settings.customRedlibInstance, "/r/privacy"),
                        InstanceProbeTarget(
                            settings.customScribeInstance,
                            "/@ftrain/big-data-small-effort-b62607a43a8c"
                        )
                    ).filter { it.instance.isNotBlank() }
                ).distinctBy { it.instance }

            _isCheckingInstances.value = true
            try {
                _instanceAvailability.value = targets.associate { target ->
                    target.instance to InstanceAvailability.CHECKING
                }
                val results = targets.map { target ->
                    async(Dispatchers.IO) {
                        target.instance to if (availabilityRepository.isAvailable(
                                target.instance,
                                target.probePath
                            )) {
                            InstanceAvailability.RESPONDING
                        } else {
                            InstanceAvailability.NOT_RESPONDING
                        }
                    }
                }.awaitAll().toMap()
                _instanceAvailability.value = results
            } finally {
                _isCheckingInstances.value = false
            }
        }
    }

}

enum class InstanceAvailability {
    CHECKING,
    RESPONDING,
    NOT_RESPONDING
}

private data class InstanceProbeTarget(val instance: String, val probePath: String)

fun normalizeInstance(instance: String): String {
    return instance
        .trim()
        .removePrefix("https://")
        .removePrefix("http://")
        .trimEnd('/')
}

fun isRedirectingInstance(instance: String): Boolean {
    val normalized = normalizeInstance(instance)
    return normalized == "twiiit.com" ||
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
