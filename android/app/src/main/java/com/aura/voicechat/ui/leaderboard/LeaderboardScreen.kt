package com.aura.voicechat.ui.leaderboard

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aura.voicechat.data.model.RankingUser
import com.aura.voicechat.data.model.VipTier
import com.aura.voicechat.ui.theme.*

/**
 * Leaderboard Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit,
    onUserClick: (String) -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val rankings by viewModel.rankings.collectAsState()
    val myRank by viewModel.myRank.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
        ) {
            // Type tabs
            ScrollableTabRow(
                selectedTabIndex = when (selectedType) {
                    "wealth" -> 0
                    "charm" -> 1
                    "level" -> 2
                    else -> 0
                },
                containerColor = DarkSurface
            ) {
                Tab(
                    selected = selectedType == "wealth",
                    onClick = { viewModel.loadLeaderboard("wealth", selectedPeriod) },
                    text = { Text("Wealth") }
                )
                Tab(
                    selected = selectedType == "charm",
                    onClick = { viewModel.loadLeaderboard("charm", selectedPeriod) },
                    text = { Text("Charm") }
                )
                Tab(
                    selected = selectedType == "level",
                    onClick = { viewModel.loadLeaderboard("level", selectedPeriod) },
                    text = { Text("Level") }
                )
            }
            
            // Period filter
            if (selectedType != "level") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = selectedPeriod == "daily",
                        onClick = { viewModel.loadLeaderboard(selectedType, "daily") },
                        label = { Text("Daily") }
                    )
                    FilterChip(
                        selected = selectedPeriod == "weekly",
                        onClick = { viewModel.loadLeaderboard(selectedType, "weekly") },
                        label = { Text("Weekly") }
                    )
                    FilterChip(
                        selected = selectedPeriod == "monthly",
                        onClick = { viewModel.loadLeaderboard(selectedType, "monthly") },
                        label = { Text("Monthly") }
                    )
                }
            }
            
            // Content
            Box(modifier = Modifier.weight(1f)) {
                when (uiState) {
                    is LeaderboardUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = PurplePrimary
                        )
                    }
                    is LeaderboardUiState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Top 3 podium
                            if (rankings.size >= 3) {
                                item {
                                    TopThreePodium(
                                        rankings = rankings.take(3),
                                        onUserClick = onUserClick
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                            
                            // Remaining ranks
                            items(rankings.drop(3)) { user ->
                                RankingUserCard(user = user, onClick = { onUserClick(user.userId) })
                            }
                        }
                    }
                    is LeaderboardUiState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                (uiState as LeaderboardUiState.Error).message,
                                color = Color.Red
                            )
                        }
                    }
                }
                
                // My rank card (sticky at bottom)
                myRank?.let { rank ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = PurplePrimary,
                        tonalElevation = 8.dp
                    ) {
                        RankingUserRow(user = rank, isHighlighted = true)
                    }
                }
            }
        }
    }
}

@Composable
fun TopThreePodium(
    rankings: List<RankingUser>,
    onUserClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd place
        if (rankings.size >= 2) {
            PodiumCard(user = rankings[1], place = 2, onClick = { onUserClick(rankings[1].userId) })
        }
        
        // 1st place
        PodiumCard(
            user = rankings[0],
            place = 1,
            onClick = { onUserClick(rankings[0].userId) },
            modifier = Modifier.padding(bottom = 20.dp)
        )
        
        // 3rd place
        if (rankings.size >= 3) {
            PodiumCard(user = rankings[2], place = 3, onClick = { onUserClick(rankings[2].userId) })
        }
    }
}

@Composable
fun PodiumCard(
    user: RankingUser,
    place: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val podiumColor = when (place) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Gray
    }
    
    Card(
        onClick = onClick,
        modifier = modifier.width(100.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Crown/Medal icon
            Icon(
                if (place == 1) Icons.Default.EmojiEvents else Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = podiumColor,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Avatar
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = user.userName,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Name
            Text(
                user.userName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
            
            // Score
            Text(
                formatScore(user.score),
                style = MaterialTheme.typography.labelSmall,
                color = podiumColor
            )
        }
    }
}

@Composable
fun RankingUserCard(
    user: RankingUser,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        RankingUserRow(user = user)
    }
}

@Composable
fun RankingUserRow(
    user: RankingUser,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank number
        Text(
            "#${user.rank}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isHighlighted) Color.White else PurplePrimary,
            modifier = Modifier.width(50.dp)
        )
        
        // Avatar
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = user.userName,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // User info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                user.userName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Lv.${user.level}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (user.vipTier != VipTier.NONE) {
                    Text(
                        user.vipTier.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = getVipColor(user.vipTier)
                    )
                }
            }
        }
        
        // Score
        Column(horizontalAlignment = Alignment.End) {
            Text(
                formatScore(user.score),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White else PurplePrimary
            )
            if (user.changeFromPrevious != 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (user.changeFromPrevious > 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (user.changeFromPrevious > 0) AccentGreen else Color.Red
                    )
                    Text(
                        "${if (user.changeFromPrevious > 0) "+" else ""}${user.changeFromPrevious}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (user.changeFromPrevious > 0) AccentGreen else Color.Red
                    )
                }
            }
        }
    }
}

private fun formatScore(score: Long): String {
    return when {
        score >= 1_000_000 -> "${score / 1_000_000}M"
        score >= 1_000 -> "${score / 1_000}K"
        else -> score.toString()
    }
}

private fun getVipColor(tier: VipTier): Color {
    return when (tier) {
        VipTier.VIP1 -> Color(0xFFC0C0C0) // Silver
        VipTier.VIP2 -> Color(0xFFFFD700) // Gold
        VipTier.VIP3 -> Color(0xFF9C27B0) // Purple
        VipTier.VIP4 -> Color(0xFF00BCD4) // Diamond
        VipTier.VIP5, VipTier.SVIP -> Color(0xFFEC4899) // Pink
        else -> Color.Gray
    }
}
