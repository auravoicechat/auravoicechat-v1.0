package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aura.voicechat.domain.model.RoomMessage
import com.aura.voicechat.ui.theme.*

/**
 * RoomChat Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Scrollable chat area displaying system messages, user messages, and gift messages
 * Auto-scrolls to bottom on new messages
 */
@Composable
fun RoomChat(
    messages: List<RoomMessage>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = DarkCard.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages) { message ->
                when (message) {
                    is RoomMessage.SystemMessage -> SystemMessageItem(message)
                    is RoomMessage.UserMessage -> UserMessageItem(message)
                    is RoomMessage.GiftMessage -> GiftMessageItem(message)
                }
            }
        }
    }
}

@Composable
private fun SystemMessageItem(message: RoomMessage.SystemMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message.text,
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary,
            modifier = Modifier
                .background(
                    color = DarkSurface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun UserMessageItem(message: RoomMessage.UserMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        // User avatar
        if (message.user.avatar != null) {
            AsyncImage(
                model = message.user.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Purple40)
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Message content
        Column(modifier = Modifier.weight(1f)) {
            // Username with level color
            val levelColor = when {
                message.user.level in 1..20 -> Color(0xFF808080)
                message.user.level in 21..40 -> Color(0xFF4CAF50)
                message.user.level in 41..60 -> Color(0xFF2196F3)
                message.user.level in 61..80 -> Color(0xFF9C27B0)
                else -> AccentGold
            }
            
            Text(
                text = "${message.user.name}:",
                style = MaterialTheme.typography.labelSmall,
                color = levelColor,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun GiftMessageItem(message: RoomMessage.GiftMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AccentMagenta.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CardGiftcard,
            contentDescription = null,
            tint = AccentMagenta,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = "${message.sender.name} sent ${message.quantity}x ${message.gift.name} to ${message.receiver.name}",
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}
