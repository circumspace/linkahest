package com.hermeticvm.linkahest.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hermeticvm.linkahest.data.models.DefaultInstances

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val instanceHealth by viewModel.instanceHealth.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(24.dp)
        ) {
            InstanceSettingsCard(
                title = "Nitter Instance",
                description = "Choose your preferred Nitter instance for Twitter/X links",
                instances = DefaultInstances.NITTER_INSTANCES,
                selectedInstance = uiState.selectedNitterInstance,
                customInstance = uiState.customNitterInstance,
                customLabel = "Custom Nitter URL",
                customPlaceholder = "e.g., nitter.example.com",
                instanceHealth = instanceHealth,
                onCheckAvailability = {
                    viewModel.checkInstances(
                        service = InstanceService.Nitter,
                        instances = DefaultInstances.NITTER_INSTANCES,
                        customInstance = uiState.customNitterInstance
                    )
                },
                onCancelAvailabilityCheck = viewModel::cancelInstanceChecks,
                onSelectInstance = viewModel::selectNitterInstance,
                onCustomInstanceChange = viewModel::updateCustomNitterInstance
            )

            InstanceSettingsCard(
                title = "Invidious Instance",
                description = "Choose your preferred Invidious instance for YouTube links",
                instances = DefaultInstances.INVIDIOUS_INSTANCES,
                selectedInstance = uiState.selectedInvidiousInstance,
                customInstance = uiState.customInvidiousInstance,
                customLabel = "Custom Invidious URL",
                customPlaceholder = "e.g., invidious.example.com",
                instanceHealth = instanceHealth,
                onCheckAvailability = {
                    viewModel.checkInstances(
                        service = InstanceService.Invidious,
                        instances = DefaultInstances.INVIDIOUS_INSTANCES,
                        customInstance = uiState.customInvidiousInstance
                    )
                },
                onCancelAvailabilityCheck = viewModel::cancelInstanceChecks,
                onSelectInstance = viewModel::selectInvidiousInstance,
                onCustomInstanceChange = viewModel::updateCustomInvidiousInstance
            )

            InstanceSettingsCard(
                title = "Reddit Frontend",
                description = "Choose your preferred privacy frontend for Reddit links",
                instances = DefaultInstances.REDLIB_INSTANCES,
                selectedInstance = uiState.selectedRedlibInstance,
                customInstance = uiState.customRedlibInstance,
                customLabel = "Custom Reddit frontend URL",
                customPlaceholder = "e.g., redlib.example.com",
                instanceHealth = instanceHealth,
                onCheckAvailability = {
                    viewModel.checkInstances(
                        service = InstanceService.Redlib,
                        instances = DefaultInstances.REDLIB_INSTANCES,
                        customInstance = uiState.customRedlibInstance
                    )
                },
                onCancelAvailabilityCheck = viewModel::cancelInstanceChecks,
                onSelectInstance = viewModel::selectRedlibInstance,
                onCustomInstanceChange = viewModel::updateCustomRedlibInstance
            )
        }
    }
}

@Composable
private fun InstanceSettingsCard(
    title: String,
    description: String,
    instances: List<String>,
    selectedInstance: String,
    customInstance: String,
    customLabel: String,
    customPlaceholder: String,
    instanceHealth: Map<String, InstanceHealth>,
    onCheckAvailability: () -> Unit,
    onCancelAvailabilityCheck: () -> Unit,
    onSelectInstance: (String) -> Unit,
    onCustomInstanceChange: (String) -> Unit
) {
    val candidateInstances = (instances + customInstance)
        .map(::normalizeInstance)
        .filter { it.isNotBlank() && !isRedirectingInstance(it) }
    val isChecking = candidateInstances.any {
        instanceHealth[it]?.status == InstanceHealthStatus.Checking
    }

    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(modifier = Modifier.selectableGroup()) {
                instances.forEach { instance ->
                    InstanceRow(
                        label = instanceLabel(instance, instanceHealth[normalizeInstance(instance)]),
                        selected = selectedInstance == instance,
                        onClick = { onSelectInstance(instance) }
                    )
                }

                InstanceRow(
                    label = "Custom instance",
                    selected = selectedInstance == "custom",
                    onClick = { onSelectInstance("custom") }
                )

                if (selectedInstance == "custom") {
                    val normalizedCustomInstance = normalizeInstance(customInstance)
                    val customHealthText = healthText(instanceHealth[normalizedCustomInstance])

                    OutlinedTextField(
                        value = customInstance,
                        onValueChange = onCustomInstanceChange,
                        label = { Text(customLabel) },
                        placeholder = { Text(customPlaceholder) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp),
                        singleLine = true
                    )

                    if (normalizedCustomInstance.isNotBlank() &&
                        !isRedirectingInstance(normalizedCustomInstance) &&
                        customHealthText.isNotBlank()
                    ) {
                        Text(
                            text = customHealthText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp)
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onCheckAvailability,
                    enabled = !isChecking,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Check availability")
                }

                if (isChecking) {
                    TextButton(onClick = onCancelAvailabilityCheck) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
private fun InstanceRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun instanceLabel(instance: String, health: InstanceHealth?): String {
    val normalized = normalizeInstance(instance)
    return when {
        normalized.startsWith("farside.link/") -> "$instance (redirecting default)"
        isRedirectingInstance(normalized) -> "$instance (redirecting)"
        else -> "$instance${healthText(health).takeIf { it.isNotBlank() }?.let { " · $it" } ?: ""}"
    }
}

private fun healthText(health: InstanceHealth?): String {
    return when (health?.status) {
        InstanceHealthStatus.Checking -> "checking"
        InstanceHealthStatus.Available -> listOfNotNull(
            "available",
            health.latencyMs?.let { "${it} ms" }
        ).joinToString(" · ")
        InstanceHealthStatus.Unavailable -> health.error?.let { "unavailable · $it" } ?: "unavailable"
        else -> ""
    }
}
