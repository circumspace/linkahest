package com.hermeticvm.linkahest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Nitter Settings
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Nitter Instance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "Choose your preferred Nitter instance for Twitter/X links",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Column(
                        modifier = Modifier.selectableGroup()
                    ) {
                        DefaultInstances.NITTER_INSTANCES.forEach { instance ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (uiState.selectedNitterInstance == instance),
                                        onClick = { viewModel.selectNitterInstance(instance) },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (uiState.selectedNitterInstance == instance),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = instance,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Custom instance option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (uiState.selectedNitterInstance == "custom"),
                                    onClick = { viewModel.selectNitterInstance("custom") },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (uiState.selectedNitterInstance == "custom"),
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Custom instance",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        if (uiState.selectedNitterInstance == "custom") {
                            OutlinedTextField(
                                value = uiState.customNitterInstance,
                                onValueChange = viewModel::updateCustomNitterInstance,
                                label = { Text("Custom Nitter URL") },
                                placeholder = { Text("e.g., nitter.example.com") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 32.dp),
                                singleLine = true
                            )
                        }
                    }
                }
            }
            
            // Invidious Settings
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Invidious Instance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "Choose your preferred Invidious instance for YouTube links",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Column(
                        modifier = Modifier.selectableGroup()
                    ) {
                        DefaultInstances.INVIDIOUS_INSTANCES.forEach { instance ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (uiState.selectedInvidiousInstance == instance),
                                        onClick = { viewModel.selectInvidiousInstance(instance) },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (uiState.selectedInvidiousInstance == instance),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = instance,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Custom instance option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (uiState.selectedInvidiousInstance == "custom"),
                                    onClick = { viewModel.selectInvidiousInstance("custom") },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (uiState.selectedInvidiousInstance == "custom"),
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Custom instance",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        if (uiState.selectedInvidiousInstance == "custom") {
                            OutlinedTextField(
                                value = uiState.customInvidiousInstance,
                                onValueChange = viewModel::updateCustomInvidiousInstance,
                                label = { Text("Custom Invidious URL") },
                                placeholder = { Text("e.g., invidious.example.com") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 32.dp),
                                singleLine = true
                            )
                        }
                    }
                }
            }
            
            // Redlib Settings
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Redlib Instance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "Choose your preferred Redlib instance for Reddit links",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Column(
                        modifier = Modifier.selectableGroup()
                    ) {
                        DefaultInstances.REDLIB_INSTANCES.forEach { instance ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (uiState.selectedRedlibInstance == instance),
                                        onClick = { viewModel.selectRedlibInstance(instance) },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (uiState.selectedRedlibInstance == instance),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = instance,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Custom instance option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (uiState.selectedRedlibInstance == "custom"),
                                    onClick = { viewModel.selectRedlibInstance("custom") },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (uiState.selectedRedlibInstance == "custom"),
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Custom instance",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        if (uiState.selectedRedlibInstance == "custom") {
                            OutlinedTextField(
                                value = uiState.customRedlibInstance,
                                onValueChange = viewModel::updateCustomRedlibInstance,
                                label = { Text("Custom Redlib URL") },
                                placeholder = { Text("e.g., redlib.example.com") },
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
    }
}