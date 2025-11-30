package com.aura.voicechat.ui.referral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.theme.*

/**
 * Referral/Invite Friends Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Features:
 * - Get Coins (invite for coins)
 * - Get Cash (invite for real money)
 * - Referral records
 * - Cash ranking
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReferralViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val clipboardManager = LocalClipboardManager.current
    
    val tabs = listOf("Get Coins", "Get Cash")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invite Friends") },
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
                .background(DarkCanvas)
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkCanvas,
                contentColor = AccentMagenta
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) AccentMagenta else TextSecondary
                            )
                        }
                    )
                }
            }
            
            when (selectedTab) {
                0 -> GetCoinsTab(
                    uiState = uiState,
                    onCopyCode = { 
                        clipboardManager.setText(AnnotatedString(uiState.inviteCode))
                    },
                    onShare = { viewModel.shareInviteLink() },
                    onBindCode = { viewModel.bindReferralCode(it) },
                    onWithdraw = { viewModel.withdrawCoins() }
                )
                1 -> GetCashTab(
                    uiState = uiState,
                    onCopyCode = {
                        clipboardManager.setText(AnnotatedString(uiState.inviteCode))
                    },
                    onShare = { viewModel.shareInviteLink() },
                    onWithdraw = { viewModel.withdrawCash() }
                )
            }
        }
    }
}

@Composable
private fun GetCoinsTab(
    uiState: ReferralUiState,
    onCopyCode: () -> Unit,
    onShare: () -> Unit,
    onBindCode: (String) -> Unit,
    onWithdraw: () -> Unit
) {
    var bindCode by remember { mutableStateOf("") }
    var showBindDialog by remember { mutableStateOf(false) }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Earnings Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(CoinGold.copy(alpha = 0.3f), DarkCard)
                            )
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Coins Earned",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = CoinGold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatNumber(uiState.totalCoinsEarned),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = CoinGold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.totalInvites}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text("Invites", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = formatNumber(uiState.availableCoins),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Text("Available", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onWithdraw,
                        enabled = uiState.availableCoins >= uiState.minWithdrawCoins,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Withdraw to Wallet")
                    }
                    
                    Text(
                        text = "Min withdraw: ${formatNumber(uiState.minWithdrawCoins)} coins",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        
        // Invite Code Card
        item {
            InviteCodeCard(
                inviteCode = uiState.inviteCode,
                onCopy = onCopyCode,
                onShare = onShare
            )
        }
        
        // Bind Code Section
        if (!uiState.hasBoundCode) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Have an invite code?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(
                                value = bindCode,
                                onValueChange = { bindCode = it },
                                placeholder = { Text("Enter code") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onBindCode(bindCode) },
                                enabled = bindCode.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta)
                            ) {
                                Text("Bind")
                            }
                        }
                    }
                }
            }
        }
        
        // Rewards Info
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "How it works",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    RewardStep(1, "Share your invite code", "Friends use your code to sign up")
                    RewardStep(2, "Friend becomes active", "They reach Level 5 and spend coins")
                    RewardStep(3, "Earn rewards", "Get 5% of their gift spending as coins")
                }
            }
        }
        
        // Recent Invites
        item {
            Text(
                text = "Recent Invites",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        
        items(uiState.recentInvites) { invite ->
            InviteRecordItem(invite = invite)
        }
    }
}

@Composable
private fun GetCashTab(
    uiState: ReferralUiState,
    onCopyCode: () -> Unit,
    onShare: () -> Unit,
    onWithdraw: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cash Earnings Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(SuccessGreen.copy(alpha = 0.3f), DarkCard)
                            )
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Cash Earned",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${String.format("%.2f", uiState.totalCashEarned)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.weeklyInvites}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text("This Week", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$${String.format("%.2f", uiState.availableCash)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Text("Available", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onWithdraw,
                        enabled = uiState.availableCash >= uiState.minWithdrawCash,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Withdraw Cash")
                    }
                    
                    Text(
                        text = "Min withdraw: $${String.format("%.2f", uiState.minWithdrawCash)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        
        // Invite Code
        item {
            InviteCodeCard(
                inviteCode = uiState.inviteCode,
                onCopy = onCopyCode,
                onShare = onShare
            )
        }
        
        // Cash Rewards Tiers
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Weekly Cash Rewards",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CashRewardTier("5 invites", "$5.00")
                    CashRewardTier("10 invites", "$12.00")
                    CashRewardTier("20 invites", "$30.00")
                    CashRewardTier("50 invites", "$100.00")
                }
            }
        }
    }
}

@Composable
private fun InviteCodeCard(
    inviteCode: String,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Invite Code",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = inviteCode,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AccentMagenta,
                    letterSpacing = androidx.compose.ui.unit.TextUnit(4f, androidx.compose.ui.unit.TextUnitType.Sp)
                )
                IconButton(onClick = onCopy) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = AccentMagenta)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onShare,
                colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Invite Link")
            }
        }
    }
}

@Composable
private fun RewardStep(step: Int, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(AccentMagenta, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$step",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            Text(description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

@Composable
private fun CashRewardTier(invites: String, reward: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(invites, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Text(reward, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SuccessGreen)
    }
}

@Composable
private fun InviteRecordItem(invite: InviteRecord) {
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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(invite.userName, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                Text(invite.date, style = MaterialTheme.typography.labelSmall, color = TextTertiary)
            }
            Text(
                text = "+${formatNumber(invite.reward)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = CoinGold
            )
        }
    }
}

// Data classes
data class InviteRecord(
    val id: String,
    val userName: String,
    val date: String,
    val reward: Long
)

private fun formatNumber(number: Long): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}
