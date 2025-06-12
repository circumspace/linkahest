package com.hermeticvm.linkahest

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Icon
        Icon(
            imageVector = Icons.Default.Link,
            contentDescription = "Linkahest Icon",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Linkahest",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Transform social media links for privacy",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "How to use:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "1. Share a YouTube, Twitter/X, or Reddit link from any app\n" +
                          "2. Choose Linkahest from the share menu\n" +
                          "3. Select your preferred transformation\n" +
                          "4. Share the transformed link",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedButton(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Settings, "Settings")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Settings")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Credits Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Credits",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Author",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "hermeticvm",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Follow on nostr",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://njump.me/npub1rfw075gc6pc693w5v568xw4mnu7umlzpkfxmqye0cgxm7qw8tauqfck3t8"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle case where no browser is available
                        }
                    }
                )
                Text(
                    text = "https://njump.me/npub1rfw075gc6pc693w5v568xw4mnu7umlzpkfxmqye0cgxm7qw8tauqfck3t8",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://njump.me/npub1rfw075gc6pc693w5v568xw4mnu7umlzpkfxmqye0cgxm7qw8tauqfck3t8"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle case where no browser is available
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Source Code",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "https://codeberg.org/hermeticvm/linkahest",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://codeberg.org/hermeticvm/linkahest"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle case where no browser is available
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Support/Donate",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Lightning donation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("lightning:hermeticvm@minibits.cash"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle case where no lightning app is available
                                    snackbarMessage = "No Lightning app found"
                                    showSnackbar = true
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡️",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.size(48.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "lightning:hermeticvm@minibits.cash",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString("hermeticvm@minibits.cash"))
                            snackbarMessage = "Lightning address copied to clipboard"
                            showSnackbar = true
                        }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy Lightning address",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Monero donation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("monero:8AuPVyudY9hRedjkRzCisrDq5rnzbUvCTckcQr5dUaGWa1yzo77uMUP8LPpSQvPBbGEktHpPqkHFPdXuCYBEL6iz9kXAhFW"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle case where no Monero app is available
                                    snackbarMessage = "No Monero app found"
                                    showSnackbar = true
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.monero256),
                            contentDescription = "Monero",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "monero:8AuPVyudY9hRedjkRzCisrDq5rnzbUvCTckcQr5dUaGWa1yzo77uMUP8LPpSQvPBbGEktHpPqkHFPdXuCYBEL6iz9kXAhFW",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString("8AuPVyudY9hRedjkRzCisrDq5rnzbUvCTckcQr5dUaGWa1yzo77uMUP8LPpSQvPBbGEktHpPqkHFPdXuCYBEL6iz9kXAhFW"))
                            snackbarMessage = "Monero address copied to clipboard"
                            showSnackbar = true
                        }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy Monero address",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Snackbar for copy feedback
        if (showSnackbar) {
            LaunchedEffect(showSnackbar) {
                kotlinx.coroutines.delay(2000)
                showSnackbar = false
            }
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }
    }
}