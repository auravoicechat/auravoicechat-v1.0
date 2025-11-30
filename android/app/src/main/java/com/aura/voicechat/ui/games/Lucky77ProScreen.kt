package com.aura.voicechat.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Lucky 77 Pro Game Screen - Premium version of Lucky 777
 * Higher stakes, higher rewards
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lucky77ProScreen(
    onNavigateBack: () -> Unit,
    viewModel: Lucky77ProViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Lucky 77 Pro")
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFFFFD700),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "VIP",
                                color = Color(0xFF1A1A2E),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A0A2E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    // Balance display
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                                ),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Diamond,
                            contentDescription = null,
                            tint = Color(0xFF1A0A2E),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "%,d".format(uiState.userDiamonds),
                            color = Color(0xFF1A0A2E),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A0A2E),
                            Color(0xFF2D1B4E),
                            Color(0xFF1A0A2E)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Jackpot Display
                JackpotDisplayPro(jackpotAmount = uiState.jackpotAmount)

                Spacer(modifier = Modifier.height(24.dp))

                // Pro Slot Machine
                ProSlotMachine(
                    slots = uiState.slots,
                    isSpinning = uiState.isSpinning
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Multiplier Selection
                MultiplierSelection(
                    selectedMultiplier = uiState.selectedMultiplier,
                    onMultiplierSelected = { viewModel.selectMultiplier(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bet Amount Selection
                BetAmountSelection(
                    selectedBet = uiState.selectedBet,
                    onBetSelected = { viewModel.selectBet(it) },
                    multiplier = uiState.selectedMultiplier
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Spin Button
                ProSpinButton(
                    enabled = uiState.canSpin,
                    isSpinning = uiState.isSpinning,
                    cost = uiState.totalCost,
                    onClick = { viewModel.spin() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recent Wins
                ProRecentWins(wins = uiState.recentWins)

                // Win Dialog
                if (uiState.showWinDialog) {
                    ProWinDialog(
                        winAmount = uiState.lastWinAmount,
                        isJackpot = uiState.isJackpot,
                        onDismiss = { viewModel.dismissWinDialog() }
                    )
                }
            }
        }
    }
}

@Composable
private fun JackpotDisplayPro(jackpotAmount: Long) {
    val infiniteTransition = rememberInfiniteTransition(label = "jackpot")
    val animatedGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 3.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFD700).copy(alpha = animatedGlow),
                        Color(0xFFFF6B6B).copy(alpha = animatedGlow),
                        Color(0xFFFFD700).copy(alpha = animatedGlow)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D1B4E)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💎 PRO JACKPOT 💎",
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "%,d".format(jackpotAmount),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            Text(
                text = "Diamonds",
                color = Color(0xFFE040FB),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ProSlotMachine(
    slots: List<String>,
    isSpinning: Boolean
) {
    val symbols = listOf("💎", "👑", "🔥", "⭐", "💰", "🎰", "77")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3D2B5E)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Slot Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(3) { index ->
                    ProSlotReel(
                        symbol = slots.getOrElse(index) { "💎" },
                        isSpinning = isSpinning
                    )
                }
            }
        }
    }
}

@Composable
private fun ProSlotReel(
    symbol: String,
    isSpinning: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isSpinning) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A0A2E), Color(0xFF2D1B4E))
                )
            )
            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 40.sp
        )
    }
}

@Composable
private fun MultiplierSelection(
    selectedMultiplier: Int,
    onMultiplierSelected: (Int) -> Unit
) {
    val multipliers = listOf(1, 2, 5, 10)

    Column {
        Text(
            text = "Multiplier",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            multipliers.forEach { multiplier ->
                FilterChip(
                    selected = selectedMultiplier == multiplier,
                    onClick = { onMultiplierSelected(multiplier) },
                    label = { Text("${multiplier}x") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE040FB),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun BetAmountSelection(
    selectedBet: Int,
    onBetSelected: (Int) -> Unit,
    multiplier: Int
) {
    val bets = listOf(100, 500, 1000, 5000)

    Column {
        Text(
            text = "Bet Amount",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            bets.forEach { bet ->
                val isSelected = selectedBet == bet
                FilterChip(
                    selected = isSelected,
                    onClick = { onBetSelected(bet) },
                    label = {
                        Text("${bet * multiplier}")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFFD700),
                        selectedLabelColor = Color(0xFF1A0A2E)
                    )
                )
            }
        }
    }
}

@Composable
private fun ProSpinButton(
    enabled: Boolean,
    isSpinning: Boolean,
    cost: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isSpinning,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE040FB),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray
        ),
        shape = RoundedCornerShape(32.dp)
    ) {
        if (isSpinning) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = Color.White
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(28.dp))
                Text(
                    text = "PRO SPIN ($cost 💎)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun ProRecentWins(wins: List<ProWinRecord>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1B4E).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "💎 Recent Pro Wins",
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (wins.isEmpty()) {
                Text(
                    text = "No recent wins",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            } else {
                wins.take(3).forEach { win ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = win.username,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "+${win.amount} 💎",
                            color = Color(0xFFE040FB),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProWinDialog(
    winAmount: Int,
    isJackpot: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2D1B4E),
        title = {
            Text(
                text = if (isJackpot) "🎊 PRO JACKPOT! 🎊" else "💎 PRO WIN! 💎",
                color = if (isJackpot) Color(0xFFFFD700) else Color(0xFFE040FB),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = if (isJackpot) 24.sp else 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "+%,d".format(winAmount),
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                )
                Text(
                    text = "Diamonds",
                    color = Color(0xFFE040FB),
                    fontSize = 18.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE040FB),
                    contentColor = Color.White
                )
            ) {
                Text("Collect")
            }
        }
    )
}

// Data classes
data class ProWinRecord(
    val username: String,
    val amount: Int,
    val timestamp: Long
)
