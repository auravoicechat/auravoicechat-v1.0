package com.aura.voicechat.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aura.voicechat.ui.theme.*

/**
 * Recharge Screen - Purchase diamonds with real money
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RechargeScreen(
    onNavigateBack: () -> Unit
) {
    var selectedPackage by remember { mutableStateOf<RechargePackage?>(null) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    
    val packages = remember {
        listOf(
            RechargePackage("pkg_100", 100, 0.99, bonus = 0),
            RechargePackage("pkg_500", 500, 4.99, bonus = 50),
            RechargePackage("pkg_1000", 1000, 9.99, bonus = 100, isPopular = true),
            RechargePackage("pkg_2000", 2000, 19.99, bonus = 300),
            RechargePackage("pkg_5000", 5000, 49.99, bonus = 1000, isBestValue = true),
            RechargePackage("pkg_10000", 10000, 99.99, bonus = 2500)
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recharge Diamonds") },
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
                .padding(16.dp)
        ) {
            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = DarkSurface
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Secure Payment",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            text = "All transactions are encrypted and secure",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Packages Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(packages) { pkg ->
                    RechargePackageCard(
                        package_ = pkg,
                        isSelected = selectedPackage == pkg,
                        onClick = {
                            selectedPackage = pkg
                            showPaymentDialog = true
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Payment Methods Info
            Text(
                text = "Accepted Payment Methods",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PaymentMethodIcon("Card")
                PaymentMethodIcon("PayPal")
                PaymentMethodIcon("GPay")
                PaymentMethodIcon("Apple")
            }
        }
    }
    
    if (showPaymentDialog && selectedPackage != null) {
        PaymentDialog(
            package_ = selectedPackage!!,
            onDismiss = { showPaymentDialog = false },
            onConfirm = {
                // TODO: Process payment
                showPaymentDialog = false
            }
        )
    }
}

@Composable
fun RechargePackageCard(
    package_: RechargePackage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PurplePrimary.copy(alpha = 0.3f) else DarkSurface
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) CardDefaults.outlinedCardBorder() else null
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Badge
            when {
                package_.isBestValue -> {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        color = Color(0xFFFFD700),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "BEST",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                package_.isPopular -> {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        color = PurplePrimary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "POPULAR",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = DiamondBlue,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${package_.diamonds}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                
                if (package_.bonus > 0) {
                    Text(
                        text = "+${package_.bonus} bonus",
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentGreen
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "$${package_.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = CoinGold,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PaymentMethodIcon(name: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurface
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun PaymentDialog(
    package_: RechargePackage,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Purchase") },
        text = {
            Column {
                Text("You are about to purchase:")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${package_.diamonds + package_.bonus} Diamonds",
                    fontWeight = FontWeight.Bold,
                    color = DiamondBlue
                )
                Text(
                    text = "for $${package_.price}",
                    fontWeight = FontWeight.Bold,
                    color = CoinGold
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Pay Now")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = DarkSurface
    )
}

data class RechargePackage(
    val id: String,
    val diamonds: Int,
    val price: Double,
    val bonus: Int = 0,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
)
