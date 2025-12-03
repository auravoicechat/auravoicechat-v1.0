package com.aura.voicechat.ui.live

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.data.model.StreamCategory
import com.aura.voicechat.ui.theme.*

/**
 * Go Live Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoLiveScreen(
    onNavigateBack: () -> Unit,
    onStreamStarted: (String) -> Unit,
    viewModel: LiveStreamViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(StreamCategory.CHAT) }
    var selectedPrivacy by remember { mutableStateOf("public") }
    var showCountdown by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsState()
    val currentStream by viewModel.currentStream.collectAsState()
    
    // Handle stream started
    LaunchedEffect(uiState) {
        if (uiState is LiveStreamUiState.StreamStarted && currentStream != null) {
            onStreamStarted(currentStream!!.id)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Go Live", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, "Close")
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Stream Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { if (it.length <= 50) title = it },
                label = { Text("Stream Title") },
                placeholder = { Text("What's your stream about?") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    Text(
                        "${title.length}/50",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PurplePrimary,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            // Category Selection
            Text(
                "Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StreamCategory.values().forEach { category ->
                    CategoryChip(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }
            
            // Privacy Settings
            Text(
                "Privacy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PrivacyOption(
                    title = "Public",
                    description = "Anyone can watch your stream",
                    icon = Icons.Default.Public,
                    isSelected = selectedPrivacy == "public",
                    onClick = { selectedPrivacy = "public" }
                )
                PrivacyOption(
                    title = "Friends Only",
                    description = "Only your friends can watch",
                    icon = Icons.Default.People,
                    isSelected = selectedPrivacy == "friends_only",
                    onClick = { selectedPrivacy = "friends_only" }
                )
                PrivacyOption(
                    title = "Private",
                    description = "Invite only",
                    icon = Icons.Default.Lock,
                    isSelected = selectedPrivacy == "private",
                    onClick = { selectedPrivacy = "private" }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Go Live Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.startStream(title, selectedCategory, selectedPrivacy)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = title.isNotBlank() && uiState !is LiveStreamUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (uiState is LiveStreamUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Icon(Icons.Default.VideoCall, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Go Live",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Error message
            if (uiState is LiveStreamUiState.Error) {
                Text(
                    (uiState as LiveStreamUiState.Error).message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun RowScope.CategoryChip(
    category: StreamCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PurplePrimary else DarkSurface,
        border = if (!isSelected) CardDefaults.outlinedCardBorder() else null
    ) {
        Text(
            category.name,
            modifier = Modifier.padding(vertical = 12.dp),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun PrivacyOption(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PurplePrimary.copy(alpha = 0.2f) else DarkSurface,
        border = if (isSelected) {
            androidx.compose.foundation.border.BorderStroke(2.dp, PurplePrimary)
        } else {
            CardDefaults.outlinedCardBorder()
        }
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
                tint = if (isSelected) PurplePrimary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = PurplePrimary
                )
            }
        }
    }
}
