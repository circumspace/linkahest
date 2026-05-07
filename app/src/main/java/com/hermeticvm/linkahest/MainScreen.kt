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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun MainScreen(
    historyEnabled: Boolean = true,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToHistorySettings: () -> Unit = onNavigateToSettings
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showCredits by remember { mutableStateOf(false) }
    var showDonations by remember { mutableStateOf(false) }
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
        Image(
            painter = painterResource(id = R.drawable.ic_linkahest_logo),
            contentDescription = "Linkahest logo",
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(24.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "LINKAHEST",
            style = MaterialTheme.typography.displayLarge.copy(
                fontFamily = FontFamily.SansSerif,
                fontSize = 64.sp,
                fontWeight = FontWeight.Black
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Transform social media links for privacy",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ElevatedCard(
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
                    text = "1. Share a link from a supported service\n" +
                          "2. Choose Linkahest from the share menu\n" +
                          "3. Select your preferred transformation\n" +
                          "4. Share the transformed link",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Supported services",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "YouTube · Twitter/X · Reddit · Medium",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        FilledTonalButton(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(Icons.Default.Settings, "Settings")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Settings",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = if (historyEnabled) onNavigateToHistory else onNavigateToHistorySettings,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (historyEnabled) 1f else 0.56f),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(Icons.Default.History, "History")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (historyEnabled) "History" else "History disabled",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        val creditsButtonText = if (showCredits) "Hide Credits" else "Show Credits"

        TextButton(onClick = { showCredits = !showCredits }, modifier = Modifier.fillMaxWidth()) {
            Text(creditsButtonText, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = showCredits,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Credits",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Author",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "hermeticvm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Follow on nostr",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "https://njump.me/npub1rfw075gc6pc693w5v568xw4mnu7umlzpkfxmqye0cgxm7qw8tauqfck3t8",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
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
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "https://github.com/circumspace/linkahest",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/circumspace/linkahest"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Handle case where no browser is available
                            }
                        }
                    )
                }
            }
        } // closes Credits AnimatedVisibility

        Spacer(modifier = Modifier.height(16.dp))

        val donationsButtonText = if (showDonations) "Hide Support" else "Show Support"

        TextButton(onClick = { showDonations = !showDonations }, modifier = Modifier.fillMaxWidth()) {
            Text(donationsButtonText, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = showDonations,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Support",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Thanks for your support",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                                        // Handle case where no Lightning app is available
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
                                text = "hermeticvm@minibits.cash",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
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
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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
                                text = "8AuPVyudY9hRedjkRzCisrDq5rnzbUvCTckcQr5dUaGWa1yzo77uMUP8LPpSQvPBbGEktHpPqkHFPdXuCYBEL6iz9kXAhFW",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
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
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        } // closes Donations AnimatedVisibility
        
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
