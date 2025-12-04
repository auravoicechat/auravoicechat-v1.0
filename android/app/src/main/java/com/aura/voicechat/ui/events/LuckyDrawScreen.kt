package com.aura.voicechat.ui.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

/**
 * Lucky Draw Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Features:
 * - Spinning wheel animation
 * - Prize display
 * - Draw history
 * - Tickets count
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyDrawScreen(
    onNavigateBack: () -> Unit,
    viewModel: LuckyDrawViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadDrawHistory()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lucky Draw") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tickets Card
            item {
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Available Tickets",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${state.remainingTickets} tickets",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Button(
                            onClick = { /* Navigate to purchase tickets */ }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Get More")
                        }
                    }
                }
            }
            
            // Spinning Wheel
            item {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Spin to Win!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LuckyWheel(
                            isSpinning = state.isSpinning,
                            prizes = state.prizes
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { viewModel.spin() },
                            enabled = !state.isSpinning && state.remainingTickets > 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            if (state.isSpinning) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Spinning...")
                            } else {
                                Icon(Icons.Default.Casino, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Spin (1 Ticket)")
                            }
                        }
                    }
                }
            }
            
            // Won Prize Display
            if (state.wonPrize != null) {
                item {
                    WonPrizeCard(prize = state.wonPrize!!)
                }
            }
            
            // Draw History
            item {
                Text(
                    text = "Draw History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (state.history.isEmpty()) {
                item {
                    Card {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No draws yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            } else {
                items(state.history) { historyItem ->
                    DrawHistoryItem(historyItem = historyItem)
                }
            }
        }
    }
}

@Composable
private fun LuckyWheel(
    isSpinning: Boolean,
    prizes: List<Prize>
) {
    var rotation by remember { mutableStateOf(0f) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "wheel")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    if (isSpinning) {
        rotation = angle
    }
    
    Box(
        modifier = Modifier
            .size(250.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Wheel
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation)
        ) {
            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)
            val segmentAngle = 360f / maxOf(prizes.size, 1)
            
            prizes.forEachIndexed { index, prize ->
                val startAngle = index * segmentAngle
                val sweepAngle = segmentAngle
                
                // Alternate colors for segments
                val color = if (index % 2 == 0) {
                    Color(0xFFFFD700) // Gold
                } else {
                    Color(0xFFFF6B6B) // Red
                }
                
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )
                
                // Border
                drawArc(
                    color = Color.White,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 2f)
                )
            }
            
            // Center circle
            drawCircle(
                color = Color.White,
                radius = 40f,
                center = center
            )
        }
        
        // Pointer
        Surface(
            modifier = Modifier
                .size(24.dp)
                .offset(y = (-125).dp)
                .align(Alignment.TopCenter),
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {}
    }
}

@Composable
private fun WonPrizeCard(prize: Prize) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFFFFD700)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Congratulations!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "You won ${prize.name}!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${prize.value} ${prize.type}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DrawHistoryItem(historyItem: DrawHistory) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = historyItem.prize.iconUrl,
                contentDescription = historyItem.prize.name,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = historyItem.prize.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "${historyItem.prize.value} ${historyItem.prize.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Text(
                text = formatDateTime(historyItem.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// Data classes
data class Prize(
    val id: String,
    val name: String,
    val iconUrl: String,
    val value: Long,
    val type: String
)

data class DrawHistory(
    val id: String,
    val prize: Prize,
    val timestamp: Long
)
