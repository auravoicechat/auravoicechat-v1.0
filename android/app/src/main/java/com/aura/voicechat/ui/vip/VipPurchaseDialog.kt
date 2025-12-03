package com.aura.voicechat.ui.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aura.voicechat.ui.theme.*

/**
 * VIP Purchase Dialog
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Composable
fun VipPurchaseDialog(
    currentTier: Int,
    onDismiss: () -> Unit,
    onPurchase: (VipPackage) -> Unit
) {
    var selectedPackage by remember { mutableStateOf<VipPackage?>(null) }
    
    val packages = remember {
        listOf(
            VipPackage(
                id = "vip1_7d",
                tier = 1,
                days = 7,
                price = 4.99,
                diamonds = 500,
                discount = 0
            ),
            VipPackage(
                id = "vip1_30d",
                tier = 1,
                days = 30,
                price = 14.99,
                diamonds = 2000,
                discount = 25,
                isPopular = true
            ),
            VipPackage(
                id = "vip2_30d",
                tier = 2,
                days = 30,
                price = 29.99,
                diamonds = 5000,
                discount = 20
            ),
            VipPackage(
                id = "vip3_30d",
                tier = 3,
                days = 30,
                price = 49.99,
                diamonds = 10000,
                discount = 30,
                isBestValue = true
            ),
            VipPackage(
                id = "vip4_30d",
                tier = 4,
                days = 30,
                price = 99.99,
                diamonds = 25000,
                discount = 35
            ),
            VipPackage(
                id = "vip5_30d",
                tier = 5,
                days = 30,
                price = 199.99,
                diamonds = 60000,
                discount = 40
            )
        ).filter { it.tier >= currentTier } // Only show current tier or higher
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkSurface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Become VIP",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Unlock exclusive benefits",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Packages List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(packages) { pkg ->
                        VipPackageCard(
                            package_ = pkg,
                            isSelected = selectedPackage == pkg,
                            onClick = { selectedPackage = pkg }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Purchase Button
                Button(
                    onClick = {
                        selectedPackage?.let {
                            onPurchase(it)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = selectedPackage != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurplePrimary,
                        disabledContainerColor = DarkCanvas
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedPackage?.let { "Purchase for $${it.price}" } ?: "Select a Package",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun VipPackageCard(
    package_: VipPackage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val vipColor = getVipColor(package_.tier)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                vipColor.copy(alpha = 0.2f) 
            else 
                DarkCanvas
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
                            text = "BEST VALUE",
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
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // VIP Badge
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(vipColor, vipColor.copy(alpha = 0.5f))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "VIP ${package_.tier}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Package Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${package_.days} Days",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = DiamondBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${package_.diamonds} Diamonds",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DiamondBlue
                        )
                    }
                    
                    if (package_.discount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${package_.discount}% Extra Bonus",
                            style = MaterialTheme.typography.bodySmall,
                            color = AccentGreen
                        )
                    }
                }
                
                // Price
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${package_.price}",
                        style = MaterialTheme.typography.titleLarge,
                        color = CoinGold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$${String.format("%.2f", package_.price / package_.days)}/day",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

data class VipPackage(
    val id: String,
    val tier: Int,
    val days: Int,
    val price: Double,
    val diamonds: Int,
    val discount: Int = 0,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
)
