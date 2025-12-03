package com.aura.voicechat.ui.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.aura.voicechat.ui.theme.*

/**
 * Block List Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Shows list of blocked users with unblock functionality
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfile: (String) -> Unit,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showUnblockDialog by remember { mutableStateOf<BlockedUser?>(null) }
    
    LaunchedEffect(Unit) {
        viewModel.loadBlockedUsers()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blocked Users") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkCanvas)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkCanvas)
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AccentMagenta
                )
            } else if (uiState.blockedUsers.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Block,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No blocked users",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "You haven't blocked anyone yet",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Info Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Blocked users cannot:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Send you messages\n• See your profile\n• Join rooms you're in",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                    
                    // Blocked Users List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.blockedUsers, key = { it.id }) { user ->
                            BlockedUserItem(
                                user = user,
                                onUserClick = { /* Blocked users can't be viewed */ },
                                onUnblockClick = { showUnblockDialog = user }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Unblock Confirmation Dialog
    showUnblockDialog?.let { user ->
        AlertDialog(
            onDismissRequest = { showUnblockDialog = null },
            icon = {
                Icon(
                    Icons.Default.Block,
                    contentDescription = null,
                    tint = AccentMagenta
                )
            },
            title = { Text("Unblock User?") },
            text = {
                Text("Are you sure you want to unblock ${user.name}? They will be able to interact with you again.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.unblockUser(user.id)
                        showUnblockDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AccentMagenta
                    )
                ) {
                    Text("Unblock")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showUnblockDialog = null }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun BlockedUserItem(
    user: BlockedUser,
    onUserClick: () -> Unit,
    onUnblockClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onUserClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar (grayed out)
                AsyncImage(
                    model = user.avatar,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f)),
                    alpha = 0.5f
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Block,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Blocked ${formatBlockedDate(user.blockedAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
            
            // Unblock Button
            OutlinedButton(
                onClick = onUnblockClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AccentMagenta
                ),
                modifier = Modifier
                    .widthIn(min = 80.dp)
                    .height(36.dp),
                shape = RoundedCornerShape(18.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(AccentMagenta)
                )
            ) {
                Text(
                    text = "Unblock",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

private fun formatBlockedDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 3600_000 -> "recently"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        diff < 604800_000 -> "${diff / 86400_000}d ago"
        else -> {
            val instant = java.time.Instant.ofEpochMilli(timestamp)
            val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")
                .withZone(java.time.ZoneId.systemDefault())
            "on ${formatter.format(instant)}"
        }
    }
}

// Data class for blocked users
data class BlockedUser(
    val id: String,
    val name: String,
    val avatar: String?,
    val blockedAt: Long,
    val reason: String? = null
)
