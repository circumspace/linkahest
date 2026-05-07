package com.hermeticvm.linkahest.ui.screens

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
                onSelectInstance = viewModel::selectInvidiousInstance,
                onCustomInstanceChange = viewModel::updateCustomInvidiousInstance
            )

            InstanceSettingsCard(
                title = "Redlib Instance",
                description = "Choose your preferred Redlib instance for Reddit links",
                instances = DefaultInstances.REDLIB_INSTANCES,
                selectedInstance = uiState.selectedRedlibInstance,
                customInstance = uiState.customRedlibInstance,
                customLabel = "Custom Redlib URL",
                customPlaceholder = "e.g., redlib.example.com",
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
    onSelectInstance: (String) -> Unit,
    onCustomInstanceChange: (String) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
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
                        label = instanceLabel(instance),
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
    androidx.compose.foundation.layout.Row(
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

private fun instanceLabel(instance: String): String {
    return when {
        instance.startsWith("farside.link/") -> "$instance (redirecting default)"
        instance == "twiiit.com" || instance == "redirect.invidious.io" -> "$instance (redirecting)"
        else -> instance
    }
}
