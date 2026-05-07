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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.hermeticvm.linkahest.data.models.ThemeModes

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
            PrivacySettingsCard(
                historyEnabled = uiState.historyEnabled,
                onHistoryEnabledChange = viewModel::updateHistoryEnabled
            )

            AppearanceSettingsCard(
                themeMode = uiState.themeMode,
                onThemeModeChange = viewModel::updateThemeMode
            )

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
                title = "Reddit Frontend",
                description = "Choose your preferred privacy frontend for Reddit links",
                instances = DefaultInstances.REDLIB_INSTANCES,
                selectedInstance = uiState.selectedRedlibInstance,
                customInstance = uiState.customRedlibInstance,
                customLabel = "Custom Reddit frontend URL",
                customPlaceholder = "e.g., redlib.example.com",
                onSelectInstance = viewModel::selectRedlibInstance,
                onCustomInstanceChange = viewModel::updateCustomRedlibInstance
            )

            InstanceSettingsCard(
                title = "Scribe Instance",
                description = "Choose your preferred Scribe instance for Medium links",
                instances = DefaultInstances.SCRIBE_INSTANCES,
                selectedInstance = uiState.selectedScribeInstance,
                customInstance = uiState.customScribeInstance,
                customLabel = "Custom Scribe URL",
                customPlaceholder = "e.g., scribe.example.com",
                onSelectInstance = viewModel::selectScribeInstance,
                onCustomInstanceChange = viewModel::updateCustomScribeInstance
            )

        }
    }
}

@Composable
private fun AppearanceSettingsCard(
    themeMode: String,
    onThemeModeChange: (String) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Choose whether Linkahest follows your device theme or stays in light or dark mode.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(modifier = Modifier.selectableGroup()) {
                ThemeModeRow(
                    label = "Auto",
                    selected = themeMode == ThemeModes.SYSTEM,
                    onClick = { onThemeModeChange(ThemeModes.SYSTEM) }
                )
                ThemeModeRow(
                    label = "Light",
                    selected = themeMode == ThemeModes.LIGHT,
                    onClick = { onThemeModeChange(ThemeModes.LIGHT) }
                )
                ThemeModeRow(
                    label = "Dark",
                    selected = themeMode == ThemeModes.DARK,
                    onClick = { onThemeModeChange(ThemeModes.DARK) }
                )
            }
        }
    }
}

@Composable
private fun ThemeModeRow(
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

@Composable
private fun PrivacySettingsCard(
    historyEnabled: Boolean,
    onHistoryEnabledChange: (Boolean) -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Save transformation history",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "When off, transformed links are not stored. Turning this off clears existing history.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = historyEnabled,
                onCheckedChange = onHistoryEnabledChange
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

private fun instanceLabel(instance: String): String {
    val normalized = normalizeInstance(instance)
    return when {
        normalized.startsWith("farside.link/") -> "$instance (redirecting default)"
        isRedirectingInstance(normalized) -> "$instance (redirecting)"
        else -> instance
    }
}
