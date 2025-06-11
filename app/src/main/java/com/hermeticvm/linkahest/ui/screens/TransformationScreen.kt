package com.hermeticvm.linkahest.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hermeticvm.linkahest.ui.components.TransformationOptionCard
import com.hermeticvm.linkahest.ui.components.UrlDisplayCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransformationScreen(
    originalUrl: String,
    viewModel: TransformationViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    
    LaunchedEffect(originalUrl) {
        viewModel.loadTransformationOptions(originalUrl)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transform Link") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = {
            if (showSnackbar) {
                SnackbarHost(
                    hostState = remember { SnackbarHostState() }
                ) {
                    LaunchedEffect(Unit) {
                        showSnackbar = false
                    }
                    Snackbar { Text(snackbarMessage) }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Original URL
            UrlDisplayCard(
                title = "Original URL",
                url = originalUrl
            )
            
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                uiState.transformationOptions.isEmpty() -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "No transformations available for this link",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                
                else -> {
                    Text(
                        text = "Choose transformation:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.transformationOptions) { option ->
                            TransformationOptionCard(
                                option = option,
                                onClick = {
                                    viewModel.transformUrl(originalUrl, option)
                                }
                            )
                        }
                    }
                }
            }
            
            // Show transformed URL if available
            uiState.transformedUrl?.let { transformedUrl ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UrlDisplayCard(
                        title = "Transformed URL (Tap to open)",
                        url = transformedUrl,
                        isClickable = true,
                        onUrlClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(transformedUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Handle case where no browser is available
                                snackbarMessage = "Unable to open link"
                                showSnackbar = true
                            }
                        }
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(transformedUrl))
                                snackbarMessage = "Copied to clipboard"
                                showSnackbar = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.ContentCopy, "Copy")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copy")
                        }
                        
                        Button(
                            onClick = {
                                val shareIntent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, transformedUrl)
                                    type = "text/plain"
                                }
                                context.startActivity(
                                    android.content.Intent.createChooser(shareIntent, "Share link")
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, "Share")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Share")
                        }
                    }
                }
            }
        }
    }
}