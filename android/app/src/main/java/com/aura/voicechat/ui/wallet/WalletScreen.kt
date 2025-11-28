package com.aura.voicechat.ui.wallet

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.domain.model.Currency
import com.aura.voicechat.domain.model.Transaction
import com.aura.voicechat.domain.model.TransactionType
import com.aura.voicechat.ui.theme.*

/**
 * Wallet Screen - Coins and Diamonds with exchange (30% rate)
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onNavigateBack: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showExchangeDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wallet") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCanvas
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkCanvas),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Balance Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Coins Card
                    BalanceCard(
                        title = "Coins",
                        balance = uiState.coins,
                        icon = Icons.Default.MonetizationOn,
                        gradientColors = listOf(CoinGold, Color(0xFFFFB700)),
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Diamonds Card
                    BalanceCard(
                        title = "Diamonds",
                        balance = uiState.diamonds,
                        icon = Icons.Default.Diamond,
                        gradientColors = listOf(DiamondBlue, AccentCyan),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        title = "Exchange",
                        icon = Icons.Default.SwapHoriz,
                        onClick = { showExchangeDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        title = "Recharge",
                        icon = Icons.Default.AddCard,
                        onClick = { /* Navigate to recharge */ },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        title = "Transfer",
                        icon = Icons.Default.Send,
                        onClick = { /* Navigate to transfer */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Exchange Rate Info
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = InfoBlue
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Exchange Rate",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                            Text(
                                text = "100,000 Diamonds → 30,000 Coins (30%)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
            
            // Transaction History Header
            item {
                Text(
                    text = "Transaction History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            
            // Transactions
            items(uiState.transactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
            
            // Empty state
            if (uiState.transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Receipt,
                                contentDescription = null,
                                tint = TextTertiary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No transactions yet",
                                color = TextTertiary
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Exchange Dialog
    if (showExchangeDialog) {
        ExchangeDialog(
            diamonds = uiState.diamonds,
            onDismiss = { showExchangeDialog = false },
            onExchange = { amount ->
                viewModel.exchangeDiamondsToCoins(amount)
                showExchangeDialog = false
            }
        )
    }
}

@Composable
private fun BalanceCard(
    title: String,
    balance: Long,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(colors = gradientColors)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatBalance(balance),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AccentMagenta,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        when (transaction.type) {
                            TransactionType.GIFT_SENT -> ErrorRed.copy(alpha = 0.2f)
                            TransactionType.GIFT_RECEIVED -> SuccessGreen.copy(alpha = 0.2f)
                            TransactionType.DAILY_REWARD -> CoinGold.copy(alpha = 0.2f)
                            TransactionType.EXCHANGE -> InfoBlue.copy(alpha = 0.2f)
                            else -> Purple80.copy(alpha = 0.2f)
                        },
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (transaction.type) {
                        TransactionType.GIFT_SENT -> Icons.Default.CardGiftcard
                        TransactionType.GIFT_RECEIVED -> Icons.Default.CardGiftcard
                        TransactionType.DAILY_REWARD -> Icons.Default.CalendarToday
                        TransactionType.EXCHANGE -> Icons.Default.SwapHoriz
                        TransactionType.VIP_PURCHASE -> Icons.Default.Star
                        TransactionType.STORE_PURCHASE -> Icons.Default.ShoppingBag
                        TransactionType.TRANSFER_IN -> Icons.Default.ArrowDownward
                        TransactionType.TRANSFER_OUT -> Icons.Default.ArrowUpward
                        TransactionType.REFERRAL_BONUS -> Icons.Default.People
                        TransactionType.EVENT_REWARD -> Icons.Default.EmojiEvents
                    },
                    contentDescription = null,
                    tint = when (transaction.type) {
                        TransactionType.GIFT_SENT -> ErrorRed
                        TransactionType.GIFT_RECEIVED -> SuccessGreen
                        TransactionType.DAILY_REWARD -> CoinGold
                        TransactionType.EXCHANGE -> InfoBlue
                        else -> Purple80
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = formatTimestamp(transaction.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
            
            // Amount
            Text(
                text = "${if (transaction.type in listOf(
                    TransactionType.GIFT_RECEIVED,
                    TransactionType.DAILY_REWARD,
                    TransactionType.TRANSFER_IN,
                    TransactionType.REFERRAL_BONUS,
                    TransactionType.EVENT_REWARD
                )) "+" else "-"}${formatBalance(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (transaction.type in listOf(
                    TransactionType.GIFT_RECEIVED,
                    TransactionType.DAILY_REWARD,
                    TransactionType.TRANSFER_IN,
                    TransactionType.REFERRAL_BONUS,
                    TransactionType.EVENT_REWARD
                )) SuccessGreen else ErrorRed
            )
        }
    }
}

@Composable
private fun ExchangeDialog(
    diamonds: Long,
    onDismiss: () -> Unit,
    onExchange: (Long) -> Unit
) {
    var inputAmount by remember { mutableStateOf("") }
    val amount = inputAmount.toLongOrNull() ?: 0L
    val coinsToReceive = (amount * 0.30).toLong()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text(
                text = "Exchange Diamonds to Coins",
                color = TextPrimary
            )
        },
        text = {
            Column {
                Text(
                    text = "Available: ${formatBalance(diamonds)} Diamonds",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = inputAmount,
                    onValueChange = { 
                        if (it.all { char -> char.isDigit() }) {
                            inputAmount = it
                        }
                    },
                    label = { Text("Diamonds to exchange") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentMagenta,
                        focusedLabelColor = AccentMagenta
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "You'll receive:",
                            color = TextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = CoinGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatBalance(coinsToReceive),
                                fontWeight = FontWeight.Bold,
                                color = CoinGold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onExchange(amount) },
                enabled = amount > 0 && amount <= diamonds,
                colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta)
            ) {
                Text("Exchange")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

private fun formatBalance(balance: Long): String {
    return when {
        balance >= 1_000_000 -> String.format("%.1fM", balance / 1_000_000.0)
        balance >= 1_000 -> String.format("%.1fK", balance / 1_000.0)
        else -> balance.toString()
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        else -> "${diff / 86400_000}d ago"
    }
}
