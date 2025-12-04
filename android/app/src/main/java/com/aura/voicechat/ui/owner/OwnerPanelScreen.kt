package com.aura.voicechat.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

/**
 * Owner Panel - Main Dashboard
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Complete control panel for app owner
 * Full access to all settings, economy, and management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerPanelScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEconomy: () -> Unit,
    onNavigateToAdmins: () -> Unit,
    onNavigateToCashouts: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    viewModel: OwnerPanelViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Owner Panel")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Owner Badge
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👑",
                            style = MaterialTheme.typography.displaySmall
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Owner Access",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Full Control Mode",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            // Quick Stats
            item {
                QuickStatsSection(state.stats)
            }
            
            // Main Sections
            item {
                Text(
                    text = "Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Economy Setup - CRITICAL
            item {
                OwnerPanelCard(
                    title = "Economy Setup",
                    subtitle = "Adjust app economy, targets, conversions",
                    icon = Icons.Default.AttachMoney,
                    iconTint = MaterialTheme.colorScheme.error,
                    badge = "CRITICAL",
                    onClick = onNavigateToEconomy
                )
            }
            
            // Admin Management
            item {
                OwnerPanelCard(
                    title = "Admin Management",
                    subtitle = "Manage all admins and permissions",
                    icon = Icons.Default.AdminPanelSettings,
                    count = state.stats.totalAdmins,
                    onClick = onNavigateToAdmins
                )
            }
            
            // Cashout Approvals
            item {
                OwnerPanelCard(
                    title = "Cashout Approvals",
                    subtitle = "Review pending withdrawal requests",
                    icon = Icons.Default.RequestQuote,
                    count = state.stats.pendingCashouts,
                    badge = if (state.stats.pendingCashouts > 0) "PENDING" else null,
                    onClick = onNavigateToCashouts
                )
            }
            
            // Guide Management
            item {
                OwnerPanelCard(
                    title = "Guide Management",
                    subtitle = "Review applications, manage guides",
                    icon = Icons.Default.Groups,
                    count = state.stats.pendingApplications,
                    onClick = onNavigateToGuides
                )
            }
            
            // Analytics
            item {
                OwnerPanelCard(
                    title = "Analytics Dashboard",
                    subtitle = "View app statistics and reports",
                    icon = Icons.Default.Analytics,
                    onClick = onNavigateToAnalytics
                )
            }
            
            item {
                Divider()
            }
            
            item {
                Text(
                    text = "Content Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(listOf(
                OwnerMenuItem("User Management", "Ban, kick, modify users", Icons.Default.People),
                OwnerMenuItem("Room Management", "Manage all rooms", Icons.Default.MeetingRoom),
                OwnerMenuItem("Event Management", "Create and manage events", Icons.Default.Event),
                OwnerMenuItem("Medal Management", "Create and assign medals", Icons.Default.EmojiEvents),
                OwnerMenuItem("VIP Tiers", "Manage VIP packages", Icons.Default.WorkspacePremium),
                OwnerMenuItem("Store Items", "Manage store catalog", Icons.Default.Store),
                OwnerMenuItem("Gift Catalog", "Manage gift items", Icons.Default.CardGiftcard),
            )) { item ->
                OwnerPanelCard(
                    title = item.title,
                    subtitle = item.subtitle,
                    icon = item.icon,
                    onClick = { /* Navigate to specific management */ }
                )
            }
            
            item {
                Divider()
            }
            
            item {
                Text(
                    text = "System Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(listOf(
                OwnerMenuItem("App Configuration", "General app settings", Icons.Default.Settings),
                OwnerMenuItem("Payment Methods", "Manage payment options", Icons.Default.Payment),
                OwnerMenuItem("Notification Settings", "Push notification config", Icons.Default.Notifications),
                OwnerMenuItem("Security Settings", "Security and privacy", Icons.Default.Security),
                OwnerMenuItem("Database Management", "Backup and restore", Icons.Default.Storage),
            )) { item ->
                OwnerPanelCard(
                    title = item.title,
                    subtitle = item.subtitle,
                    icon = item.icon,
                    onClick = { /* Navigate to specific settings */ }
                )
            }
        }
    }
}

@Composable
private fun QuickStatsSection(stats: OwnerStats) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Quick Stats",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Users", stats.totalUsers.toString())
                StatItem("Admins", stats.totalAdmins.toString())
                StatItem("Guides", stats.totalGuides.toString())
                StatItem("Active", stats.activeRooms.toString())
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OwnerPanelCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    count: Int? = null,
    badge: String? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = iconTint
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (badge != null) {
                        Surface(
                            color = if (badge == "CRITICAL") 
                                MaterialTheme.colorScheme.error 
                            else MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = badge,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (badge == "CRITICAL")
                                    MaterialTheme.colorScheme.onError
                                else MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            if (count != null && count > 0) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = count.toString(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

data class OwnerMenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)
