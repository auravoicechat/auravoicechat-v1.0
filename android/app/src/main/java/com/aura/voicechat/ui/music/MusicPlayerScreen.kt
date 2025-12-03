package com.aura.voicechat.ui.music

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.aura.voicechat.data.model.Song
import com.aura.voicechat.ui.theme.*

/**
 * Music Player Screen - Full player interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    onNavigateBack: () -> Unit,
    viewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val isShuffleEnabled by viewModel.isShuffleEnabled.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    
    currentSong?.let { song ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Now Playing") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ExpandMore, "Minimize")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Show queue */ }) {
                            Icon(Icons.Default.QueueMusic, "Queue")
                        }
                        IconButton(onClick = { /* Show options */ }) {
                            Icon(Icons.Default.MoreVert, "More")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = DarkCanvas
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Gradient background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    PurplePrimary.copy(alpha = 0.3f),
                                    DarkCanvas
                                )
                            )
                        )
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Album Art
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        AsyncImage(
                            model = song.albumArt,
                            contentDescription = song.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Song Info
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            song.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            song.artist,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Progress Bar
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Slider(
                            value = currentPosition.toFloat(),
                            onValueChange = { viewModel.seekTo(it.toLong()) },
                            valueRange = 0f..song.duration.toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = PurplePrimary,
                                activeTrackColor = PurplePrimary,
                                inactiveTrackColor = Color.Gray
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                formatDuration(currentPosition),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            Text(
                                formatDuration(song.duration),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Playback Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Shuffle
                        IconButton(onClick = { viewModel.toggleShuffle() }) {
                            Icon(
                                Icons.Default.Shuffle,
                                "Shuffle",
                                tint = if (isShuffleEnabled) PurplePrimary else Color.Gray,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        // Previous
                        IconButton(onClick = { viewModel.playPrevious() }) {
                            Icon(
                                Icons.Default.SkipPrevious,
                                "Previous",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        
                        // Play/Pause
                        FloatingActionButton(
                            onClick = {
                                if (isPlaying) viewModel.pauseSong()
                                else viewModel.resumeSong()
                            },
                            containerColor = PurplePrimary,
                            modifier = Modifier.size(72.dp)
                        ) {
                            Icon(
                                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        
                        // Next
                        IconButton(onClick = { viewModel.playNext() }) {
                            Icon(
                                Icons.Default.SkipNext,
                                "Next",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        
                        // Repeat
                        IconButton(onClick = { viewModel.toggleRepeatMode() }) {
                            Icon(
                                when (repeatMode) {
                                    RepeatMode.ONE -> Icons.Default.RepeatOne
                                    else -> Icons.Default.Repeat
                                },
                                "Repeat",
                                tint = when (repeatMode) {
                                    RepeatMode.OFF -> Color.Gray
                                    else -> PurplePrimary
                                },
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Additional Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = { /* Add to favorites */ }) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                "Favorite",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Add to playlist */ }) {
                            Icon(
                                Icons.Default.PlaylistAdd,
                                "Add to Playlist",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Share */ }) {
                            Icon(
                                Icons.Default.Share,
                                "Share",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No song playing", color = Color.Gray)
    }
}

@Composable
fun MiniPlayer(
    song: Song,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onExpand),
        color = DarkSurface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album Art
            AsyncImage(
                model = song.albumArt,
                contentDescription = song.title,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Song Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    song.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
            
            // Play/Pause Button
            IconButton(onClick = onPlayPause) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    if (isPlaying) "Pause" else "Play",
                    tint = PurplePrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return String.format("%d:%02d", minutes, seconds)
}
