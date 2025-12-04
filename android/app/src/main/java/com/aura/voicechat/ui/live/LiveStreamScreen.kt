package com.aura.voicechat.ui.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aura.voicechat.data.model.LiveStream
import com.aura.voicechat.ui.theme.*

/**
 * Live Stream Screen - Watch stream with chat overlay
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamScreen(
    streamId: String,
    onNavigateBack: () -> Unit,
    viewModel: LiveStreamViewModel = hiltViewModel()
) {
    val currentStream by viewModel.currentStream.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    var showChat by remember { mutableStateOf(true) }
    var chatMessage by remember { mutableStateOf("") }
    
    LaunchedEffect(streamId) {
        viewModel.loadStreamDetails(streamId)
    }
    
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Video player area
        currentStream?.let { stream ->
            // Video player placeholder - integrate with your video player library
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for video player
                AsyncImage(
                    model = stream.thumbnailUrl,
                    contentDescription = stream.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Loading or error state
                if (uiState is LiveStreamUiState.Loading) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            }
            
            // Overlay UI
            Column(modifier = Modifier.fillMaxSize()) {
                // Top bar with stream info
                TopStreamBar(
                    stream = stream,
                    onBack = onNavigateBack,
                    onShare = { /* Share logic */ }
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Chat overlay (if visible)
                if (showChat) {
                    ChatOverlay(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
                
                // Bottom controls
                BottomStreamControls(
                    chatMessage = chatMessage,
                    onChatMessageChange = { chatMessage = it },
                    onSendMessage = {
                        // Send message logic
                        chatMessage = ""
                    },
                    onToggleChat = { showChat = !showChat },
                    onSendGift = { /* Gift sending logic */ },
                    onLike = { /* Like animation */ }
                )
            }
        }
    }
}

@Composable
fun TopStreamBar(
    stream: LiveStream,
    onBack: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Host info
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = stream.hostAvatarUrl,
                contentDescription = stream.hostName,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    stream.hostName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Red
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Circle,
                            contentDescription = null,
                            modifier = Modifier.size(8.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "LIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        // Viewer count
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    stream.viewerCount.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Share button
        IconButton(onClick = onShare) {
            Icon(Icons.Default.Share, "Share", tint = Color.White)
        }
    }
}

@Composable
fun ChatOverlay(modifier: Modifier = Modifier) {
    val chatMessages = remember {
        // Mock chat messages - replace with real chat data
        listOf(
            ChatMessage("User1", "Hello everyone! 👋", System.currentTimeMillis()),
            ChatMessage("User2", "Great stream! 🔥", System.currentTimeMillis()),
            ChatMessage("User3", "Amazing!", System.currentTimeMillis())
        )
    }
    
    val listState = rememberLazyListState()
    
    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true
    ) {
        items(chatMessages) { message ->
            ChatMessageItem(message)
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Surface(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.8f),
        shape = RoundedCornerShape(12.dp),
        color = Color.Black.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                message.userName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun BottomStreamControls(
    chatMessage: String,
    onChatMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onToggleChat: () -> Unit,
    onSendGift: () -> Unit,
    onLike: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.7f)
                    )
                )
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chat input
        OutlinedTextField(
            value = chatMessage,
            onValueChange = onChatMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Say something...", color = Color.Gray) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                containerColor = Color.Black.copy(alpha = 0.5f),
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            trailingIcon = {
                if (chatMessage.isNotBlank()) {
                    IconButton(onClick = onSendMessage) {
                        Icon(Icons.Default.Send, "Send", tint = PurplePrimary)
                    }
                }
            }
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Gift button
        IconButton(
            onClick = onSendGift,
            modifier = Modifier
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.CardGiftcard, "Gift", tint = Color(0xFFFFB700))
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Like button
        IconButton(
            onClick = onLike,
            modifier = Modifier
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.Favorite, "Like", tint = Color.Red)
        }
    }
}

data class ChatMessage(
    val userName: String,
    val text: String,
    val timestamp: Long
)
