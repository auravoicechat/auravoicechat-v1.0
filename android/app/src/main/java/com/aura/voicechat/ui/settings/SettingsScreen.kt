package com.aura.voicechat.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.theme.*

/**
 * Settings Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToBlocked: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkCanvas)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkCanvas)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Account Section
            item {
                SettingsSection(title = "Account") {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Edit Profile",
                        subtitle = "Change name, avatar, bio",
                        onClick = { /* Navigate to edit profile */ }
                    )
                    SettingsItem(
                        icon = Icons.Default.Lock,
                        title = "Privacy",
                        subtitle = "Who can see your profile",
                        onClick = onNavigateToPrivacy
                    )
                    SettingsItem(
                        icon = Icons.Default.Block,
                        title = "Blocked Users",
                        subtitle = "${uiState.blockedCount} users blocked",
                        onClick = onNavigateToBlocked
                    )
                }
            }
            
            // Notifications Section
            item {
                SettingsSection(title = "Notifications") {
                    SettingsToggle(
                        icon = Icons.Default.Notifications,
                        title = "Push Notifications",
                        isChecked = uiState.pushNotifications,
                        onToggle = { viewModel.togglePushNotifications() }
                    )
                    SettingsToggle(
                        icon = Icons.Default.CardGiftcard,
                        title = "Gift Notifications",
                        isChecked = uiState.giftNotifications,
                        onToggle = { viewModel.toggleGiftNotifications() }
                    )
                    SettingsToggle(
                        icon = Icons.Default.Message,
                        title = "Message Notifications",
                        isChecked = uiState.messageNotifications,
                        onToggle = { viewModel.toggleMessageNotifications() }
                    )
                }
            }
            
            // Audio/Video Section
            item {
                SettingsSection(title = "Audio & Video") {
                    SettingsToggle(
                        icon = Icons.Default.Mic,
                        title = "Noise Cancellation",
                        isChecked = uiState.noiseCancellation,
                        onToggle = { viewModel.toggleNoiseCancellation() }
                    )
                    SettingsToggle(
                        icon = Icons.Default.VolumeUp,
                        title = "Auto-Play Animations",
                        isChecked = uiState.autoPlayAnimations,
                        onToggle = { viewModel.toggleAutoPlayAnimations() }
                    )
                }
            }
            
            // General Section
            item {
                SettingsSection(title = "General") {
                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = "Language",
                        subtitle = uiState.language,
                        onClick = { viewModel.showLanguageDialog() }
                    )
                    SettingsItem(
                        icon = Icons.Default.Storage,
                        title = "Clear Cache",
                        subtitle = "${uiState.cacheSize} MB",
                        onClick = { viewModel.clearCache() }
                    )
                }
            }
            
            // Support Section
            item {
                SettingsSection(title = "Support") {
                    SettingsItem(
                        icon = Icons.Default.Help,
                        title = "Help Center",
                        onClick = { /* Open help */ }
                    )
                    SettingsItem(
                        icon = Icons.Default.Feedback,
                        title = "Send Feedback",
                        onClick = { /* Open feedback */ }
                    )
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About",
                        subtitle = "Version ${uiState.appVersion}",
                        onClick = onNavigateToAbout
                    )
                }
            }
            
            // Logout Section
            item {
                SettingsSection(title = "Account Actions") {
                    SettingsItem(
                        icon = Icons.Default.Logout,
                        title = "Log Out",
                        titleColor = WarningOrange,
                        onClick = { showLogoutDialog = true }
                    )
                    SettingsItem(
                        icon = Icons.Default.DeleteForever,
                        title = "Delete Account",
                        titleColor = ErrorRed,
                        onClick = { showDeleteAccountDialog = true }
                    )
                }
            }
        }
    }
    
    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = DarkCard,
            title = { Text("Log Out", color = TextPrimary) },
            text = { Text("Are you sure you want to log out?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
    
    // Delete Account Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            containerColor = DarkCard,
            title = { Text("Delete Account", color = ErrorRed) },
            text = { 
                Text(
                    "This action cannot be undone. All your data, including coins, diamonds, and items will be permanently deleted.",
                    color = TextSecondary
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        viewModel.deleteAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Delete Forever")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp)
            )
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = TextPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AccentMagenta,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = titleColor
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}

@Composable
private fun SettingsToggle(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AccentMagenta,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AccentMagenta,
                checkedTrackColor = AccentMagenta.copy(alpha = 0.5f),
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = DarkSurface
            )
        )
    }
}
