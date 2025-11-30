package com.aura.voicechat.ui.level

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.theme.*

/**
 * User Level System Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Shows user level progress and level-up rewards
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    onNavigateBack: () -> Unit,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Level") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(paddingValues)
        ) {
            // Level Header
            item {
                LevelHeader(
                    currentLevel = uiState.currentLevel,
                    currentExp = uiState.currentExp,
                    requiredExp = uiState.requiredExp,
                    totalExp = uiState.totalExp
                )
            }
            
            // How to earn EXP
            item {
                EarnExpCard()
            }
            
            // Level Rewards
            item {
                Text(
                    text = "Level Rewards",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            items(uiState.levelRewards) { reward ->
                LevelRewardItem(
                    reward = reward,
                    currentLevel = uiState.currentLevel,
                    onClaim = { viewModel.claimReward(reward.level) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun LevelHeader(
    currentLevel: Int,
    currentExp: Long,
    requiredExp: Long,
    totalExp: Long
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            VipGold.copy(alpha = 0.3f),
                            DarkCard
                        )
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Level Badge
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(VipGold, VipGold.copy(alpha = 0.7f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = DarkCanvas,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Lv.$currentLevel",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkCanvas
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress to next level
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "EXP to Lv.${currentLevel + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "${formatNumber(currentExp)} / ${formatNumber(requiredExp)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = VipGold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (currentExp.toFloat() / requiredExp).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = VipGold,
                    trackColor = DarkSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Total EXP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Total EXP: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = formatNumber(totalExp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = VipGold
                )
            }
        }
    }
}

@Composable
private fun EarnExpCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "How to Earn EXP",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ExpSourceRow(icon = Icons.Default.CardGiftcard, text = "Send gifts", exp = "+1 EXP per coin")
            ExpSourceRow(icon = Icons.Default.Mic, text = "Speak in rooms", exp = "+5 EXP per minute")
            ExpSourceRow(icon = Icons.Default.Login, text = "Daily login", exp = "+100 EXP")
            ExpSourceRow(icon = Icons.Default.EmojiEvents, text = "Complete tasks", exp = "Varies")
        }
    }
}

@Composable
private fun ExpSourceRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    exp: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AccentMagenta,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = exp,
            style = MaterialTheme.typography.bodySmall,
            color = SuccessGreen
        )
    }
}

@Composable
private fun LevelRewardItem(
    reward: LevelReward,
    currentLevel: Int,
    onClaim: () -> Unit
) {
    val isUnlocked = currentLevel >= reward.level
    val canClaim = isUnlocked && !reward.isClaimed
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) DarkCard else DarkSurface
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Level indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) VipGold.copy(alpha = 0.2f) else DarkCanvas
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${reward.level}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) VipGold else TextTertiary
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Level ${reward.level} Reward",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isUnlocked) TextPrimary else TextTertiary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (reward.coins > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = CoinGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = formatNumber(reward.coins),
                                style = MaterialTheme.typography.labelSmall,
                                color = CoinGold
                            )
                        }
                    }
                    if (reward.cosmetic != null) {
                        Text(
                            text = "+ ${reward.cosmetic}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentMagenta
                        )
                    }
                }
            }
            
            when {
                reward.isClaimed -> {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                canClaim -> {
                    Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Claim", style = MaterialTheme.typography.labelMedium)
                    }
                }
                else -> {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// Data class
data class LevelReward(
    val level: Int,
    val coins: Long,
    val cosmetic: String?,
    val isClaimed: Boolean
)

private fun formatNumber(number: Long): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}
