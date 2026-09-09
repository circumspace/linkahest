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
    val unavailableReason: String? = null,
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
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                unavailableReason = null,
                transformedUrl = null,
                error = null
            )
            
            try {
                val options = transformLinkUseCase.getAvailableTransformations(url)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    transformationOptions = options,
                    unavailableReason = transformLinkUseCase.getUnavailableReason(url),
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unable to check transformations for this link."
                )
            }
        }
    }
    
    fun transformUrl(originalUrl: String, option: TransformationOption) {
        viewModelScope.launch {
            try {
                val transformedUrl = transformLinkUseCase.transformAndSave(originalUrl, option)
                _uiState.value = if (transformedUrl == originalUrl) {
                    _uiState.value.copy(
                        transformedUrl = null,
                        error = transformLinkUseCase.getUnavailableReason(originalUrl)
                            ?: "This link could not be transformed. Try another link format or target."
                    )
                } else {
                    _uiState.value.copy(
                        transformedUrl = transformedUrl,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unable to transform this link."
                )
            }
        }
    }
}
