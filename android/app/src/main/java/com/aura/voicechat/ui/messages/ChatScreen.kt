package com.aura.voicechat.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.aura.voicechat.ui.theme.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Chat Screen - Private messaging between two users
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Features:
 * - Message list with different bubble styles for sent/received
 * - Support for text, image, voice, and gift messages
 * - Input bar with emoji, image, voice record, and gift buttons
 * - User info header with online status
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: String,
    recipientId: String,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    var messageText by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var showGiftPanel by remember { mutableStateOf(false) }
    
    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId)
        viewModel.markAsRead(conversationId)
    }
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onNavigateToProfile(recipientId) }
                    ) {
                        Box {
                            AsyncImage(
                                model = uiState.recipient?.avatar,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Purple40.copy(alpha = 0.2f))
                            )
                            
                            if (uiState.recipient?.isOnline == true) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .align(Alignment.BottomEnd)
                                        .background(Color.Green, CircleShape)
                                        .border(2.dp, DarkCanvas, CircleShape)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = uiState.recipient?.name ?: "Loading...",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (uiState.recipient?.isOnline == true) "Online" else "Offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (uiState.recipient?.isOnline == true) Color.Green else TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Voice call */ }) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call")
                    }
                    IconButton(onClick = { /* Video call */ }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call")
                    }
                    IconButton(onClick = { /* More options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkCanvas)
            )
        },
        bottomBar = {
            ChatInputBar(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendMessage = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendTextMessage(conversationId, messageText)
                        messageText = ""
                    }
                },
                onEmojiClick = { showEmojiPicker = !showEmojiPicker },
                onImageClick = { viewModel.sendImageMessage(conversationId) },
                onVoiceClick = { /* Start voice recording */ },
                onGiftClick = { showGiftPanel = !showGiftPanel }
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
            } else if (uiState.messages.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ChatBubble,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No messages yet",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Start a conversation!",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.messages, key = { it.id }) { message ->
                        MessageBubble(
                            message = message,
                            isSent = message.senderId == "me" // TODO: Use actual current user ID
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onEmojiClick: () -> Unit,
    onImageClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onGiftClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.05f),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Emoji button
            IconButton(onClick = onEmojiClick) {
                Icon(
                    Icons.Default.EmojiEmotions,
                    contentDescription = "Emoji",
                    tint = TextSecondary
                )
            }
            
            // Text input
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentMagenta,
                    unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                    focusedContainerColor = Color.White.copy(alpha = 0.05f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                ),
                maxLines = 4
            )
            
            if (messageText.isBlank()) {
                // Extra buttons when no text
                IconButton(onClick = onImageClick) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "Image",
                        tint = TextSecondary
                    )
                }
                
                IconButton(onClick = onVoiceClick) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Voice",
                        tint = TextSecondary
                    )
                }
                
                IconButton(onClick = onGiftClick) {
                    Icon(
                        Icons.Default.CardGiftcard,
                        contentDescription = "Gift",
                        tint = AccentMagenta
                    )
                }
            } else {
                // Send button
                IconButton(
                    onClick = onSendMessage,
                    modifier = Modifier
                        .size(48.dp)
                        .background(AccentMagenta, CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: MessageState,
    isSent: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isSent) Alignment.End else Alignment.Start
    ) {
        when (message.type) {
            MessageType.TEXT -> TextMessageBubble(message, isSent)
            MessageType.IMAGE -> ImageMessageBubble(message, isSent)
            MessageType.VOICE -> VoiceMessageBubble(message, isSent)
            MessageType.GIFT -> GiftMessageBubble(message, isSent)
        }
        
        // Timestamp
        Text(
            text = formatMessageTime(message.timestamp),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun TextMessageBubble(message: MessageState, isSent: Boolean) {
    Surface(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = if (isSent) 16.dp else 4.dp,
            bottomEnd = if (isSent) 4.dp else 16.dp
        ),
        color = if (isSent) AccentMagenta else Color.White.copy(alpha = 0.1f)
    ) {
        Text(
            text = message.content,
            modifier = Modifier.padding(12.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ImageMessageBubble(message: MessageState, isSent: Boolean) {
    AsyncImage(
        model = message.content,
        contentDescription = "Image message",
        modifier = Modifier
            .widthIn(max = 280.dp)
            .heightIn(max = 300.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
    )
}

@Composable
private fun VoiceMessageBubble(message: MessageState, isSent: Boolean) {
    Surface(
        modifier = Modifier
            .widthIn(min = 200.dp, max = 280.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSent) AccentMagenta.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = AccentMagenta
            )
            Spacer(modifier = Modifier.width(8.dp))
            
            // Waveform placeholder
            repeat(20) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height((10..30).random().dp)
                        .background(AccentMagenta.copy(alpha = 0.6f))
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = message.duration ?: "0:00",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun GiftMessageBubble(message: MessageState, isSent: Boolean) {
    Surface(
        modifier = Modifier
            .widthIn(max = 200.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFD700).copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CardGiftcard,
                contentDescription = "Gift",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message.giftName ?: "Gift",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            message.giftValue?.let {
                Text(
                    text = "$it 💎",
                    style = MaterialTheme.typography.bodySmall,
                    color = AccentMagenta
                )
            }
        }
    }
}

private fun formatMessageTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

// Data classes
enum class MessageType {
    TEXT, IMAGE, VOICE, GIFT
}

data class MessageState(
    val id: String,
    val senderId: String,
    val content: String,
    val type: MessageType,
    val timestamp: Long,
    val duration: String? = null, // For voice messages
    val giftName: String? = null, // For gift messages
    val giftValue: Long? = null, // For gift messages
    val isRead: Boolean = false
)

data class RecipientState(
    val id: String,
    val name: String,
    val avatar: String?,
    val isOnline: Boolean
)
