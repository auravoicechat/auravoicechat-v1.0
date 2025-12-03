package com.aura.voicechat.ui.live

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aura.voicechat.data.model.LiveStream
import com.aura.voicechat.data.model.StreamCategory
import com.aura.voicechat.ui.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Live Stream List Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamListScreen(
    onNavigateBack: () -> Unit,
    onStreamClick: (String) -> Unit,
    onGoLiveClick: () -> Unit,
    viewModel: LiveStreamViewModel = hiltViewModel()
) {
    val streams by viewModel.streams.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    val categories = listOf(
        null to "All",
        StreamCategory.CHAT to "Chat",
        StreamCategory.MUSIC to "Music",
        StreamCategory.GAMING to "Gaming",
        StreamCategory.TALENT to "Talent",
        StreamCategory.PARTY to "Party"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Streams", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshStreams() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onGoLiveClick,
                icon = { Icon(Icons.Default.VideoCall, "Go Live") },
                text = { Text("Go Live") },
                containerColor = PurplePrimary
            )
        },
        containerColor = DarkCanvas
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Category filters
            ScrollableTabRow(
                selectedTabIndex = categories.indexOfFirst { it.first == selectedCategory },
                containerColor = DarkSurface,
                edgePadding = 16.dp
            ) {
                categories.forEach { (category, name) ->
                    Tab(
                        selected = category == selectedCategory,
                        onClick = { viewModel.loadLiveStreams(category) },
                        text = { Text(name) }
                    )
                }
            }
            
            // Content
            when (uiState) {
                is LiveStreamUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                    }
                }
                is LiveStreamUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.VideoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No live streams right now",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onGoLiveClick,
                                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                            ) {
                                Text("Be the first to go live!")
                            }
                        }
                    }
                }
                is LiveStreamUiState.Error -> {
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
                                (uiState as LiveStreamUiState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.refreshStreams() },
                                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                else -> {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(uiState is LiveStreamUiState.Loading),
                        onRefresh = { viewModel.refreshStreams() }
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(streams) { stream ->
                                LiveStreamCard(
                                    stream = stream,
                                    onClick = { onStreamClick(stream.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveStreamCard(
    stream: LiveStream,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Thumbnail
            AsyncImage(
                model = stream.thumbnailUrl,
                contentDescription = stream.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            
            // Live badge
            if (stream.isLive) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Red
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Viewer count
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(4.dp),
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        formatViewerCount(stream.viewerCount),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
            
            // Stream info at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                // Category badge
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = getCategoryColor(stream.category)
                ) {
                    Text(
                        stream.category.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Stream title
                Text(
                    stream.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                // Host name with avatar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = stream.hostAvatarUrl,
                        contentDescription = stream.hostName,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        stream.hostName,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun formatViewerCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${count / 1_000_000}M"
        count >= 1_000 -> "${count / 1_000}K"
        else -> count.toString()
    }
}

private fun getCategoryColor(category: StreamCategory): Color {
    return when (category) {
        StreamCategory.CHAT -> Color(0xFF7C3AED) // Purple
        StreamCategory.MUSIC -> Color(0xFFEC4899) // Pink
        StreamCategory.GAMING -> Color(0xFF3B82F6) // Blue
        StreamCategory.TALENT -> Color(0xFF10B981) // Green
        StreamCategory.PARTY -> Color(0xFFF59E0B) // Orange
    }
}
