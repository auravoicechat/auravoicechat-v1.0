package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aura.voicechat.ui.theme.DarkCard
import com.aura.voicechat.ui.theme.TextPrimary
import com.aura.voicechat.ui.theme.AccentCyan

/**
 * RoomHeader Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Header bar for room screen with back button, room name, listener count, and action buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomHeader(
    roomName: String,
    listenerCount: Int,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = roomName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.RemoveRedEye,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = listenerCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onShare) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = TextPrimary
                )
            }
            IconButton(onClick = onSettings) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Settings",
                    tint = TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkCard.copy(alpha = 0.9f)
        ),
        modifier = modifier
    )
}
