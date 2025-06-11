package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.TransformationOption
import com.hermeticvm.linkahest.domain.usecases.TransformLinkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TransformationUiState(
    val isLoading: Boolean = false,
    val transformationOptions: List<TransformationOption> = emptyList(),
    val transformedUrl: String? = null,
    val error: String? = null
)

class TransformationViewModel(
    private val transformLinkUseCase: TransformLinkUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TransformationUiState())
    val uiState: StateFlow<TransformationUiState> = _uiState.asStateFlow()
    
    fun loadTransformationOptions(url: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val options = transformLinkUseCase.getAvailableTransformations(url)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    transformationOptions = options,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun transformUrl(originalUrl: String, option: TransformationOption) {
        viewModelScope.launch {
            try {
                val transformedUrl = transformLinkUseCase.transformAndSave(originalUrl, option)
                _uiState.value = _uiState.value.copy(
                    transformedUrl = transformedUrl,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }
}