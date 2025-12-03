package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aura.voicechat.ui.theme.*

/**
 * RoomBottomBar Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Bottom control bar with chat input, emoji button, gift button, and mic toggle
 */
@Composable
fun RoomBottomBar(
    isMuted: Boolean,
    onSendMessage: (String) -> Unit,
    onEmojiClick: () -> Unit,
    onGiftClick: () -> Unit,
    onMicToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var chatMessage by remember { mutableStateOf("") }
    
    BottomAppBar(
        containerColor = DarkSurface,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Chat input field
            OutlinedTextField(
                value = chatMessage,
                onValueChange = { chatMessage = it },
                placeholder = {
                    Text(
                        text = "Say something...",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkCard,
                    unfocusedContainerColor = DarkCard,
                    focusedBorderColor = AccentMagenta,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                textStyle = MaterialTheme.typography.bodySmall,
                singleLine = true,
                trailingIcon = {
                    if (chatMessage.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                if (chatMessage.isNotBlank()) {
                                    onSendMessage(chatMessage)
                                    chatMessage = ""
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send",
                                tint = AccentMagenta
                            )
                        }
                    }
                }
            )
            
            // Emoji button
            IconButton(
                onClick = onEmojiClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(DarkCard, CircleShape)
            ) {
                Icon(
                    Icons.Default.EmojiEmotions,
                    contentDescription = "Emoji",
                    tint = AccentCyan
                )
            }
            
            // Gift button
            IconButton(
                onClick = onGiftClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(DarkCard, CircleShape)
            ) {
                Icon(
                    Icons.Default.CardGiftcard,
                    contentDescription = "Send Gift",
                    tint = AccentGold
                )
            }
            
            // Mic toggle button
            IconButton(
                onClick = onMicToggle,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isMuted) ErrorRed else AccentCyan,
                        CircleShape
                    )
            ) {
                Icon(
                    if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = if (isMuted) "Unmute" else "Mute",
                    tint = Color.White
                )
            }
        }
    }
}
