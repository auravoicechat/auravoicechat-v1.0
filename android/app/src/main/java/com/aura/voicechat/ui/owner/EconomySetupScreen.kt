package com.aura.voicechat.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Economy Setup Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * CRITICAL: Owner can adjust all app economy settings
 * - Earning targets
 * - Conversion rates
 * - Prices
 * - Cashout limits
 * - VIP packages
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EconomySetupScreen(
    onNavigateBack: () -> Unit,
    viewModel: EconomySetupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Economy Setup") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Warning Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError
                    )
                    
                    Text(
                        text = "⚠️ CRITICAL: Changes affect entire app economy",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
            
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Targets") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Conversions") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Prices") }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Limits") }
                )
            }
            
            // Content
            when (selectedTab) {
                0 -> TargetsTab(viewModel)
                1 -> ConversionsTab(viewModel)
                2 -> PricesTab(viewModel)
                3 -> LimitsTab(viewModel)
            }
        }
    }
}

@Composable
private fun TargetsTab(viewModel: EconomySetupViewModel) {
    val state by viewModel.state.collectAsState()
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Earning Targets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Set diamond requirements and cash rewards for each tier",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isGuideTargets by remember { mutableStateOf(false) }
                
                FilterChip(
                    selected = !isGuideTargets,
                    onClick = { isGuideTargets = false },
                    label = { Text("User Targets") }
                )
                
                FilterChip(
                    selected = isGuideTargets,
                    onClick = { isGuideTargets = true },
                    label = { Text("Guide Targets") }
                )
            }
        }
        
        items(state.userTargets) { target ->
            TargetEditor(
                target = target,
                onUpdate = { viewModel.updateTarget(it) }
            )
        }
        
        item {
            Button(
                onClick = { viewModel.addNewTarget() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Tier")
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.saveTargets() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save All Changes")
            }
        }
    }
}

@Composable
private fun TargetEditor(
    target: TargetConfig,
    onUpdate: (TargetConfig) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = target.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tier ${target.tier}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                
                var name by remember { mutableStateOf(target.name) }
                var diamonds by remember { mutableStateOf(target.requiredDiamonds.toString()) }
                var cash by remember { mutableStateOf(target.cashReward.toString()) }
                var coins by remember { mutableStateOf(target.bonusCoins?.toString() ?: "0") }
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tier Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = diamonds,
                    onValueChange = { diamonds = it },
                    label = { Text("Required Diamonds") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Text("💎") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = cash,
                    onValueChange = { cash = it },
                    label = { Text("Cash Reward (USD)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    leadingIcon = { Text("$") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = coins,
                    onValueChange = { coins = it },
                    label = { Text("Bonus Coins") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Text("🪙") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { expanded = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            onUpdate(target.copy(
                                name = name,
                                requiredDiamonds = diamonds.toLongOrNull() ?: 0,
                                cashReward = cash.toDoubleOrNull() ?: 0.0,
                                bonusCoins = coins.toLongOrNull()
                            ))
                            expanded = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update")
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversionsTab(viewModel: EconomySetupViewModel) {
    val state by viewModel.state.collectAsState()
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Conversion Rates",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            ConversionRateCard(
                title = "Diamond to Cash",
                description = "How many diamonds = $1 USD",
                currentRate = state.diamondToCashRate.toString(),
                unit = "💎 per $1",
                onUpdate = { viewModel.updateDiamondToCashRate(it) }
            )
        }
        
        item {
            ConversionRateCard(
                title = "Diamond to Coin",
                description = "Conversion rate for diamond → coin exchange",
                currentRate = "${state.diamondToCoinRate}%",
                unit = "% of diamonds",
                onUpdate = { viewModel.updateDiamondToCoinRate(it) }
            )
        }
        
        item {
            ConversionRateCard(
                title = "Coin Value",
                description = "How many coins = $1 USD",
                currentRate = state.coinValue.toString(),
                unit = "🪙 per $1",
                onUpdate = { viewModel.updateCoinValue(it) }
            )
        }
        
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Current Conversion Examples",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("100,000 💎 = $${state.diamondToCashRate * 100000 / 1000000}")
                    Text("100,000 💎 = ${(100000 * state.diamondToCoinRate / 100).toLong()} 🪙")
                    Text("30,000 🪙 = $${state.coinValue * 30000 / 1000000}")
                }
            }
        }
    }
}

@Composable
private fun ConversionRateCard(
    title: String,
    description: String,
    currentRate: String,
    unit: String,
    onUpdate: (String) -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(currentRate) }
    
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                IconButton(onClick = { editing = !editing }) {
                    Icon(
                        if (editing) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = null
                    )
                }
            }
            
            if (editing) {
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("New Rate") },
                    suffix = { Text(unit) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = {
                        onUpdate(value)
                        editing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Rate")
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "$currentRate $unit",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PricesTab(viewModel: EconomySetupViewModel) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "App Prices",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Text("VIP Package Prices", style = MaterialTheme.typography.titleMedium)
        }
        
        items(listOf("VIP", "SVIP", "VVIP")) { tier ->
            PriceEditor(
                title = "$tier Monthly",
                currentPrice = "$9.99",
                onUpdate = { }
            )
        }
        
        item {
            Divider()
            Text("Gift Prices", style = MaterialTheme.typography.titleMedium)
        }
        
        item {
            Text(
                text = "Manage gift catalog prices individually from Store Management",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun PriceEditor(
    title: String,
    currentPrice: String,
    onUpdate: (String) -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = currentPrice,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun LimitsTab(viewModel: EconomySetupViewModel) {
    val state by viewModel.state.collectAsState()
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "System Limits",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            LimitEditor(
                title = "Min Cashout Amount",
                currentValue = state.minCashout.toString(),
                unit = "USD",
                onUpdate = { viewModel.updateMinCashout(it) }
            )
        }
        
        item {
            LimitEditor(
                title = "Max Cashout Amount",
                currentValue = state.maxCashout.toString(),
                unit = "USD",
                onUpdate = { viewModel.updateMaxCashout(it) }
            )
        }
        
        item {
            LimitEditor(
                title = "Clearance Period",
                currentValue = state.clearanceDays.toString(),
                unit = "days",
                onUpdate = { viewModel.updateClearanceDays(it) }
            )
        }
        
        item {
            LimitEditor(
                title = "Max Gift Per Send",
                currentValue = state.maxGiftQuantity.toString(),
                unit = "items",
                onUpdate = { viewModel.updateMaxGiftQuantity(it) }
            )
        }
        
        item {
            LimitEditor(
                title = "Daily Diamond Limit",
                currentValue = state.dailyDiamondLimit.toString(),
                unit = "💎",
                onUpdate = { viewModel.updateDailyDiamondLimit(it) }
            )
        }
    }
}

@Composable
private fun LimitEditor(
    title: String,
    currentValue: String,
    unit: String,
    onUpdate: (String) -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(currentValue) }
    
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = { editing = !editing }) {
                    Icon(
                        if (editing) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = null
                    )
                }
            }
            
            if (editing) {
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("New Value") },
                    suffix = { Text(unit) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = {
                        onUpdate(value)
                        editing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update")
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "$currentValue $unit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Data classes
data class TargetConfig(
    val tier: Int,
    val name: String,
    val requiredDiamonds: Long,
    val cashReward: Double,
    val bonusCoins: Long?
)
