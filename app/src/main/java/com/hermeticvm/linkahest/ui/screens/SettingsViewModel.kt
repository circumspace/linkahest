package com.hermeticvm.linkahest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hermeticvm.linkahest.data.models.UserSettings
import com.hermeticvm.linkahest.data.repository.SettingsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.HttpURLConnection
import java.net.URL

enum class InstanceHealthStatus {
    Unchecked,
    Checking,
    Available,
    Unavailable
}

data class InstanceHealth(
    val status: InstanceHealthStatus = InstanceHealthStatus.Unchecked,
    val latencyMs: Long? = null,
    val error: String? = null
)

enum class InstanceService(val testPath: String) {
    Nitter("/jack"),
    Invidious("/watch?v=dQw4w9WgXcQ"),
    Redlib("/r/popular")
}

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserSettings())
    val uiState: StateFlow<UserSettings> = _uiState.asStateFlow()

    private val _instanceHealth = MutableStateFlow<Map<String, InstanceHealth>>(emptyMap())
    val instanceHealth: StateFlow<Map<String, InstanceHealth>> = _instanceHealth.asStateFlow()

    private var availabilityCheckJob: Job? = null
    
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

    fun checkInstances(
        service: InstanceService,
        instances: List<String>,
        customInstance: String = ""
    ) {
        cancelInstanceChecks()

        val directInstances = (instances + customInstance)
            .map { normalizeInstance(it) }
            .filter { it.isNotBlank() && !isRedirectingInstance(it) }
            .distinct()

        if (directInstances.isEmpty()) return

        _instanceHealth.value = _instanceHealth.value + directInstances.associateWith {
            InstanceHealth(status = InstanceHealthStatus.Checking)
        }

        availabilityCheckJob = viewModelScope.launch {
            try {
                directInstances.map { instance ->
                    async(Dispatchers.IO) {
                        instance to checkDirectInstance(instance, service.testPath)
                    }
                }.awaitAll().forEach { (instance, health) ->
                    _instanceHealth.value = _instanceHealth.value + (instance to health)
                }
            } catch (e: CancellationException) {
                _instanceHealth.value = _instanceHealth.value.mapValues { (_, health) ->
                    if (health.status == InstanceHealthStatus.Checking) {
                        InstanceHealth()
                    } else {
                        health
                    }
                }
                throw e
            }
        }
    }

    fun cancelInstanceChecks() {
        availabilityCheckJob?.cancel()
        availabilityCheckJob = null
        _instanceHealth.value = _instanceHealth.value.mapValues { (_, health) ->
            if (health.status == InstanceHealthStatus.Checking) {
                InstanceHealth()
            } else {
                health
            }
        }
    }

    private suspend fun checkDirectInstance(instance: String, testPath: String): InstanceHealth {
        return try {
            withContext(Dispatchers.IO) {
                withTimeout(2_500) {
                    requestInstance(instance, testPath, "HEAD")
                        ?: requestInstance(instance, testPath, "GET")
                        ?: InstanceHealth(
                            status = InstanceHealthStatus.Unavailable,
                            error = "No response"
                        )
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            InstanceHealth(
                status = InstanceHealthStatus.Unavailable,
                error = e.message
            )
        }
    }

    private fun requestInstance(
        instance: String,
        testPath: String,
        method: String
    ): InstanceHealth? {
        val start = System.nanoTime()
        val connection = (URL("https://$instance$testPath").openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 1_500
            readTimeout = 1_500
            instanceFollowRedirects = true
        }

        return try {
            val code = connection.responseCode
            val latencyMs = (System.nanoTime() - start) / 1_000_000

            when {
                code in 200..399 -> InstanceHealth(
                    status = InstanceHealthStatus.Available,
                    latencyMs = latencyMs
                )
                method == "HEAD" && code in setOf(403, 405) -> null
                else -> InstanceHealth(
                    status = InstanceHealthStatus.Unavailable,
                    latencyMs = latencyMs,
                    error = "HTTP $code"
                )
            }
        } finally {
            connection.disconnect()
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
