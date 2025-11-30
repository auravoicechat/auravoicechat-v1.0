package com.aura.voicechat.ui.dailyreward

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.theme.*

/**
 * Daily Login Reward Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRewardScreen(
    onNavigateBack: () -> Unit,
    onClaimReward: () -> Unit,
    viewModel: DailyRewardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showClaimAnimation by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Rewards") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkCanvas)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkCanvas, Color(0xFF1A0A2E))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Streak Display
            StreakCard(
                currentStreak = uiState.currentStreak,
                longestStreak = uiState.longestStreak
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Login every day to earn rewards!",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Days Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(uiState.rewards) { reward ->
                    DayRewardCard(
                        reward = reward,
                        isToday = reward.day == uiState.currentDay,
                        onClick = {
                            if (reward.day == uiState.currentDay && !reward.isClaimed && uiState.canClaimToday) {
                                showClaimAnimation = true
                                viewModel.claimReward()
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Today's Reward Detail
            TodayRewardDetail(
                reward = uiState.rewards.find { it.day == uiState.currentDay },
                canClaim = uiState.canClaimToday,
                onClaim = {
                    showClaimAnimation = true
                    viewModel.claimReward()
                }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bonus Info
            BonusInfo()
        }
    }
    
    if (showClaimAnimation) {
        ClaimAnimationOverlay(
            onAnimationComplete = {
                showClaimAnimation = false
                onClaimReward()
            }
        )
    }
}

@Composable
private fun StreakCard(currentStreak: Int, longestStreak: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🔥", style = MaterialTheme.typography.headlineMedium)
                Text("$currentStreak", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = AccentMagenta)
                Text("Current Streak", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Divider(modifier = Modifier.height(80.dp).width(1.dp), color = DarkSurface)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆", style = MaterialTheme.typography.headlineMedium)
                Text("$longestStreak", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = VipGold)
                Text("Longest Streak", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun DayRewardCard(reward: DailyReward, isToday: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isToday && !reward.isClaimed) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = when {
                reward.isClaimed -> SuccessGreen.copy(alpha = 0.2f)
                isToday -> AccentMagenta.copy(alpha = 0.2f)
                else -> DarkCard
            }
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(80.dp).scale(scale).then(
            if (isToday && !reward.isClaimed) Modifier.border(2.dp, AccentMagenta, RoundedCornerShape(12.dp)) else Modifier
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (reward.day == 7) "Day 7 🎁" else "Day ${reward.day}",
                style = MaterialTheme.typography.labelSmall,
                color = if (isToday) AccentMagenta else TextSecondary,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(
                    when {
                        reward.isClaimed -> SuccessGreen.copy(alpha = 0.3f)
                        reward.day == 7 -> VipGold.copy(alpha = 0.3f)
                        else -> DarkSurface
                    }
                ),
                contentAlignment = Alignment.Center
            ) {
                if (reward.isClaimed) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen)
                } else {
                    Text(
                        text = when (reward.rewardType) {
                            "coins" -> "💰"
                            "diamonds" -> "💎"
                            "frame" -> "🖼️"
                            "vehicle" -> "🚗"
                            else -> "🎁"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = reward.displayAmount,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = when {
                    reward.isClaimed -> SuccessGreen
                    reward.day == 7 -> VipGold
                    else -> CoinGold
                }
            )
        }
    }
}

@Composable
private fun TodayRewardDetail(reward: DailyReward?, canClaim: Boolean, onClaim: () -> Unit) {
    if (reward == null) return
    Card(
        colors = CardDefaults.cardColors(containerColor = if (canClaim && !reward.isClaimed) AccentMagenta.copy(alpha = 0.1f) else DarkCard),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Today's Reward", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = when (reward.rewardType) { "coins" -> "💰"; "diamonds" -> "💎"; "frame" -> "🖼️"; "vehicle" -> "🚗"; else -> "🎁" },
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(reward.displayAmount, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = CoinGold)
            if (reward.bonusItem != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("+ ${reward.bonusItem}", style = MaterialTheme.typography.bodyMedium, color = AccentMagenta)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClaim,
                enabled = canClaim && !reward.isClaimed,
                colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta, disabledContainerColor = DarkSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when { reward.isClaimed -> "Claimed ✓"; canClaim -> "Claim Now!"; else -> "Come back tomorrow" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BonusInfo() {
    Card(colors = CardDefaults.cardColors(containerColor = DarkCard.copy(alpha = 0.7f)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("💡 Login every day for bigger rewards!", style = MaterialTheme.typography.bodySmall, color = TextSecondary, textAlign = TextAlign.Center)
            Text("Day 7 has special bonus items!", style = MaterialTheme.typography.labelSmall, color = VipGold)
        }
    }
}

@Composable
private fun ClaimAnimationOverlay(onAnimationComplete: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "claim")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1.5f,
        animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Restart),
        label = "scale"
    )
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(1500); onAnimationComplete() }
    Box(modifier = Modifier.fillMaxSize().background(DarkCanvas.copy(alpha = 0.8f)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉", style = MaterialTheme.typography.displayLarge, modifier = Modifier.scale(scale))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Reward Claimed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = VipGold)
        }
    }
}

data class DailyReward(
    val day: Int,
    val rewardType: String,
    val amount: Long,
    val displayAmount: String,
    val bonusItem: String? = null,
    val isClaimed: Boolean = false
)
