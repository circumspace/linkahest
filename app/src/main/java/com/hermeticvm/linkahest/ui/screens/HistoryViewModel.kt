package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.LinkTransformation
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: LinkTransformationRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    val historyEnabled: StateFlow<Boolean> = settingsRepository.userSettings
        .map { it.historyEnabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val transformations: StateFlow<List<LinkTransformation>> = combine(
        repository.getAllTransformations(),
        settingsRepository.userSettings.map { it.historyEnabled }
    ) { transformations, enabled ->
        if (enabled) transformations else emptyList()
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            settingsRepository.userSettings
                .map { it.historyEnabled }
                .distinctUntilChanged()
                .collect { enabled ->
                    if (!enabled) {
                        repository.clearAllTransformations()
                    }
                }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAllTransformations()
        }
    }
}

class HistoryViewModelFactory(
    private val repository: LinkTransformationRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
