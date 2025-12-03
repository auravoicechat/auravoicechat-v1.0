package com.aura.voicechat.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.domain.model.Currency
import com.aura.voicechat.domain.model.Transaction
import com.aura.voicechat.domain.model.TransactionType
import com.aura.voicechat.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Transaction History Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf<TransactionFilter>(TransactionFilter.ALL) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCanvas
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkCanvas)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransactionFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurplePrimary,
                            selectedLabelColor = Color.White,
                            containerColor = DarkSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
            
            Divider(color = DarkSurface)
            
            // Transactions List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            } else {
                val filteredTransactions = when (selectedFilter) {
                    TransactionFilter.ALL -> uiState.transactions
                    TransactionFilter.INCOME -> uiState.transactions.filter {
                        it.type in listOf(
                            TransactionType.GIFT_RECEIVED,
                            TransactionType.DAILY_REWARD,
                            TransactionType.REFERRAL_BONUS,
                            TransactionType.EVENT_REWARD,
                            TransactionType.TRANSFER_IN
                        )
                    }
                    TransactionFilter.EXPENSE -> uiState.transactions.filter {
                        it.type in listOf(
                            TransactionType.GIFT_SENT,
                            TransactionType.VIP_PURCHASE,
                            TransactionType.STORE_PURCHASE,
                            TransactionType.EXCHANGE,
                            TransactionType.TRANSFER_OUT
                        )
                    }
                }
                
                if (filteredTransactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = TextSecondary
                            )
                            Text(
                                text = "No transactions yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTransactions) { transaction ->
                            TransactionItem(transaction)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val isIncome = transaction.type in listOf(
        TransactionType.GIFT_RECEIVED,
        TransactionType.DAILY_REWARD,
        TransactionType.REFERRAL_BONUS,
        TransactionType.EVENT_REWARD,
        TransactionType.TRANSFER_IN
    )
    
    val (icon, iconColor) = when (transaction.type) {
        TransactionType.GIFT_SENT, TransactionType.GIFT_RECEIVED -> 
            Icons.Default.CardGiftcard to Color(0xFFE91E63)
        TransactionType.DAILY_REWARD -> 
            Icons.Default.Stars to Color(0xFFFFD700)
        TransactionType.VIP_PURCHASE -> 
            Icons.Default.Workspace Premium to PurplePrimary
        TransactionType.STORE_PURCHASE -> 
            Icons.Default.ShoppingCart to AccentCyan
        TransactionType.EXCHANGE -> 
            Icons.Default.SwapHoriz to Color(0xFF9C27B0)
        TransactionType.TRANSFER_IN, TransactionType.TRANSFER_OUT -> 
            Icons.Default.SwapVert to Color(0xFF00BCD4)
        TransactionType.REFERRAL_BONUS -> 
            Icons.Default.PersonAdd to AccentGreen
        TransactionType.EVENT_REWARD -> 
            Icons.Default.Event to Color(0xFFFF9800)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDate(transaction.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                if (transaction.relatedUserName != null) {
                    Text(
                        text = transaction.relatedUserName,
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentCyan
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Amount
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isIncome) "+" else "-",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isIncome) AccentGreen else Color(0xFFEF5350),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${transaction.amount}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isIncome) AccentGreen else Color(0xFFEF5350),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (transaction.currency) {
                            Currency.COINS -> Icons.Default.MonetizationOn
                            Currency.DIAMONDS -> Icons.Default.Diamond
                            Currency.USD -> Icons.Default.AttachMoney
                        },
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = when (transaction.currency) {
                            Currency.COINS -> CoinGold
                            Currency.DIAMONDS -> DiamondBlue
                            Currency.USD -> AccentGreen
                        }
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = transaction.currency.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

enum class TransactionFilter(val displayName: String) {
    ALL("All"),
    INCOME("Income"),
    EXPENSE("Expense")
}
