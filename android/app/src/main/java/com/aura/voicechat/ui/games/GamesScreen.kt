package com.aura.voicechat.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.theme.*

/**
 * Games Hub Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Games:
 * - Gift Wheel System
 * - Lucky 777 Game
 * - Lucky 77 Pro Game
 * - Lucky Fruit Game
 * - Greedy Baby Game
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGame: (String) -> Unit,
    viewModel: GamesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Games") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = CoinGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatNumber(uiState.coins),
                            style = MaterialTheme.typography.labelMedium,
                            color = CoinGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkCanvas)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkCanvas)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Jackpot Banner
            item {
                JackpotBanner(jackpot = uiState.currentJackpot)
            }
            
            // Games List
            item {
                Text(
                    text = "Available Games",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            
            items(uiState.games) { game ->
                GameCard(
                    game = game,
                    onClick = { onNavigateToGame(game.id) }
                )
            }
            
            // Recent Wins
            item {
                Text(
                    text = "Recent Big Wins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            items(uiState.recentWins) { win ->
                RecentWinItem(win = win)
            }
        }
    }
}

@Composable
private fun JackpotBanner(jackpot: Long) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = VipGold.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎰 JACKPOT 🎰",
                style = MaterialTheme.typography.titleMedium,
                color = VipGold,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatNumber(jackpot),
                style = MaterialTheme.typography.headlineLarge,
                color = VipGold,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "coins",
                style = MaterialTheme.typography.bodyMedium,
                color = VipGold.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun GameCard(
    game: GameInfo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Game Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(game.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = game.emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = game.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = CoinGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Min: ${game.minBet}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CoinGold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Max Win: ${game.maxWinMultiplier}x",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessGreen
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextTertiary
            )
        }
    }
}

@Composable
private fun RecentWinItem(win: RecentWin) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(win.gameEmoji, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = win.userName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = win.gameName,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+${formatNumber(win.amount)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
                Text(
                    text = win.timeAgo,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }
        }
    }
}

// Data classes
data class GameInfo(
    val id: String,
    val name: String,
    val description: String,
    val emoji: String,
    val color: Color,
    val minBet: Long,
    val maxWinMultiplier: Int
)

data class RecentWin(
    val id: String,
    val userName: String,
    val gameName: String,
    val gameEmoji: String,
    val amount: Long,
    val timeAgo: String
)

private fun formatNumber(number: Long): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}
