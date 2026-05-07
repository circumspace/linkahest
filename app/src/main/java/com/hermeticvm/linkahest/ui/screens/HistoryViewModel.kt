package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.LinkTransformation
import com.hermeticvm.linkahest.data.repository.LinkTransformationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: LinkTransformationRepository
) : ViewModel() {
    val transformations: StateFlow<List<LinkTransformation>> = repository
        .getAllTransformations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAllTransformations()
        }
    }
}

class HistoryViewModelFactory(
    private val repository: LinkTransformationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
