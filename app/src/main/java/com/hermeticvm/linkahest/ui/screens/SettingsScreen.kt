package com.hermeticvm.linkahest.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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
    val instanceAvailability by viewModel.instanceAvailability.collectAsState()
    val isCheckingInstances by viewModel.isCheckingInstances.collectAsState()
    val context = LocalContext.current
    var networkPermissionDenied by remember { mutableStateOf(false) }
    var showNetworkPermissionExplanation by remember { mutableStateOf(false) }
    val appSettingsLauncher = rememberLauncherForActivityResult(StartActivityForResult()) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
        networkPermissionDenied = !granted
        if (granted) viewModel.checkInstances()
    }

    if (showNetworkPermissionExplanation) {
        AlertDialog(
            onDismissRequest = { showNetworkPermissionExplanation = false },
            title = { Text("Network access required") },
            text = {
                Text(
                    "This system requires Network to be enabled manually after an app update adds it. Open App info, allow Network under Permissions, then return to Linkahest."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNetworkPermissionExplanation = false
                        appSettingsLauncher.launch(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${context.packageName}")
                            )
                        )
                    }
                ) {
                    Text("Open App info")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNetworkPermissionExplanation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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

            InstanceCheckCard(
                isChecking = isCheckingInstances,
                permissionDenied = networkPermissionDenied,
                onCheck = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.INTERNET
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        networkPermissionDenied = false
                        viewModel.checkInstances()
                    } else {
                        showNetworkPermissionExplanation = true
                    }
                }
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
                onCustomInstanceChange = viewModel::updateCustomNitterInstance,
                availability = instanceAvailability
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
                onCustomInstanceChange = viewModel::updateCustomInvidiousInstance,
                availability = instanceAvailability
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
                onCustomInstanceChange = viewModel::updateCustomRedlibInstance,
                availability = instanceAvailability
            )

            InstanceSettingsCard(
                title = "Medium Frontend",
                description = "Choose your preferred privacy frontend for Medium links",
                instances = DefaultInstances.SCRIBE_INSTANCES,
                selectedInstance = uiState.selectedScribeInstance,
                customInstance = uiState.customScribeInstance,
                customLabel = "Custom Medium frontend URL",
                customPlaceholder = "e.g., medium.example.com",
                onSelectInstance = viewModel::selectScribeInstance,
                onCustomInstanceChange = viewModel::updateCustomScribeInstance,
                availability = instanceAvailability
            )

        }
    }
}

@Composable
private fun InstanceCheckCard(
    isChecking: Boolean,
    permissionDenied: Boolean,
    onCheck: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Instance availability", style = MaterialTheme.typography.titleMedium)
            Text(
                "Request a sample page from every built-in instance and custom address. A response does not guarantee that every page works. Results are not saved.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (permissionDenied) {
                Text(
                    "Network access is denied. Allow Network in Linkahest's app permissions to check instances.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(onClick = onCheck, enabled = !isChecking) {
                if (isChecking) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (isChecking) "Checking..." else "Check all instances")
            }
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
    onCustomInstanceChange: (String) -> Unit,
    availability: Map<String, InstanceAvailability>
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
                        availability = availability[instance],
                        selected = selectedInstance == instance,
                        onClick = { onSelectInstance(instance) }
                    )
                }

                InstanceRow(
                    label = "Custom instance",
                    availability = availability[customInstance],
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
    availability: InstanceAvailability?,
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
        Column {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            availability?.let {
                Text(
                    text = when (it) {
                        InstanceAvailability.CHECKING -> "Checking"
                        InstanceAvailability.RESPONDING -> "Responding"
                        InstanceAvailability.NOT_RESPONDING -> "Not responding"
                    },
                    color = when (it) {
                        InstanceAvailability.RESPONDING -> MaterialTheme.colorScheme.primary
                        InstanceAvailability.NOT_RESPONDING -> MaterialTheme.colorScheme.error
                        InstanceAvailability.CHECKING -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun instanceLabel(instance: String): String {
    val normalized = normalizeInstance(instance)
    return when {
        isRedirectingInstance(normalized) -> "$instance (redirecting)"
        else -> instance
    }
}
