package com.aura.voicechat.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aura.voicechat.data.model.AppNotification
import com.aura.voicechat.data.model.NotificationType
import com.aura.voicechat.ui.theme.*
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.text.SimpleDateFormat
import java.util.*

/**
 * Notification Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Notifications")
                        if (unreadCount > 0) {
                            Text(
                                "$unreadCount unread",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.markAllAsRead() }) {
                        Icon(Icons.Default.DoneAll, "Mark all as read")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        },
        containerColor = DarkCanvas
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { 
                        selectedTab = 0
                        viewModel.loadNotifications()
                    },
                    text = { Text("All") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { 
                        selectedTab = 1
                        viewModel.loadNotifications(NotificationType.MENTION)
                    },
                    text = { Text("Mentions") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { 
                        selectedTab = 2
                        viewModel.loadNotifications(NotificationType.SYSTEM)
                    },
                    text = { Text("System") }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { 
                        selectedTab = 3
                        viewModel.loadNotifications(NotificationType.GIFT)
                    },
                    text = { Text("Gifts") }
                )
            }
            
            // Content
            when (uiState) {
                is NotificationUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                    }
                }
                is NotificationUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No notifications",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    }
                }
                is NotificationUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(notifications, key = { it.id }) { notification ->
                            SwipeableActionsBox(
                                swipeThreshold = 100.dp,
                                endActions = listOf(
                                    SwipeAction(
                                        onSwipe = { viewModel.deleteNotification(notification.id) },
                                        icon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.White,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        },
                                        background = Color.Red
                                    )
                                )
                            ) {
                                NotificationItem(
                                    notification = notification,
                                    onClick = {
                                        if (!notification.isRead) {
                                            viewModel.markAsRead(notification.id)
                                        }
                                        onNotificationClick(notification)
                                    }
                                )
                            }
                            Divider(color = Color.Gray.copy(alpha = 0.2f))
                        }
                    }
                }
                is NotificationUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                (uiState as NotificationUiState.Error).message,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.loadNotifications() },
                                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = if (notification.isRead) Color.Transparent else PurplePrimary.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on type
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = getNotificationColor(notification.type).copy(alpha = 0.2f)
            ) {
                Icon(
                    getNotificationIcon(notification.type),
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = getNotificationColor(notification.type)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        notification.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                        color = Color.White
                    )
                    if (!notification.isRead) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PurplePrimary)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formatTimestamp(notification.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            // Image if available
            notification.imageUrl?.let { imageUrl ->
                Spacer(modifier = Modifier.width(12.dp))
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.MENTION -> Icons.Default.AlternateEmail
        NotificationType.GIFT -> Icons.Default.CardGiftcard
        NotificationType.FOLLOW -> Icons.Default.PersonAdd
        NotificationType.SYSTEM -> Icons.Default.Info
        NotificationType.ROOM_INVITE -> Icons.Default.MeetingRoom
        NotificationType.CP_REQUEST -> Icons.Default.Favorite
        NotificationType.FAMILY_INVITE -> Icons.Default.Groups
        NotificationType.EVENT -> Icons.Default.Event
        NotificationType.DAILY_REWARD -> Icons.Default.EmojiEvents
    }
}

private fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.MENTION -> AccentCyan
        NotificationType.GIFT -> Color(0xFFFFB700)
        NotificationType.FOLLOW -> AccentGreen
        NotificationType.SYSTEM -> Color.Gray
        NotificationType.ROOM_INVITE -> PurplePrimary
        NotificationType.CP_REQUEST -> Color(0xFFEC4899)
        NotificationType.FAMILY_INVITE -> Color(0xFF9C27B0)
        NotificationType.EVENT -> Color(0xFFF59E0B)
        NotificationType.DAILY_REWARD -> Color(0xFFFFD700)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 604800000 -> "${diff / 86400000}d ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    }
}
