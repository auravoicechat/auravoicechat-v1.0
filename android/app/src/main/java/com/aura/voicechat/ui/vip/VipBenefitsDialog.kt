package com.aura.voicechat.ui.vip

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aura.voicechat.ui.theme.*

/**
 * VIP Benefits Dialog
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Composable
fun VipBenefitsDialog(
    tier: Int,
    onDismiss: () -> Unit
) {
    val benefits = getVipBenefits(tier)
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
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
                        tint = getVipColor(tier),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "VIP $tier Benefits",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${benefits.size} exclusive perks",
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
                
                // Benefits List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(benefits) { benefit ->
                        BenefitItem(benefit)
                    }
                }
            }
        }
    }
}

@Composable
fun BenefitItem(benefit: VipBenefit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = benefit.color.copy(alpha = 0.2f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = benefit.icon,
                    contentDescription = null,
                    tint = benefit.color,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = benefit.title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = benefit.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

fun getVipBenefits(tier: Int): List<VipBenefit> {
    val baseBenefits = listOf(
        VipBenefit(
            icon = Icons.Default.Badge,
            title = "VIP Badge",
            description = "Exclusive VIP badge displayed on your profile",
            color = Color(0xFFFFD700)
        ),
        VipBenefit(
            icon = Icons.Default.Stars,
            title = "Daily Rewards Boost",
            description = "${tier * 10}% bonus on daily rewards",
            color = Color(0xFFFF9800)
        ),
        VipBenefit(
            icon = Icons.Default.TrendingUp,
            title = "XP Boost",
            description = "${tier * 5}% faster leveling",
            color = AccentGreen
        )
    )
    
    val tierBenefits = when (tier) {
        1 -> listOf(
            VipBenefit(
                icon = Icons.Default.Chat,
                title = "Custom Chat Bubble",
                description = "Access to VIP chat bubble designs",
                color = AccentCyan
            ),
            VipBenefit(
                icon = Icons.Default.EmojiEmotions,
                title = "Exclusive Emojis",
                description = "5 VIP-only emoji packs",
                color = Color(0xFFE91E63)
            )
        )
        2 -> listOf(
            VipBenefit(
                icon = Icons.Default.Frame,
                title = "Profile Frames",
                description = "10 exclusive VIP frames",
                color = PurplePrimary
            ),
            VipBenefit(
                icon = Icons.Default.Fireplace,
                title = "Room Entry Effect",
                description = "Special VIP entry animation",
                color = Color(0xFFFF5722)
            ),
            VipBenefit(
                icon = Icons.Default.PersonAdd,
                title = "More Friends",
                description = "Friend list capacity +50",
                color = Color(0xFF2196F3)
            )
        )
        3, 4, 5 -> listOf(
            VipBenefit(
                icon = Icons.Default.Diamond,
                title = "Diamond Bonus",
                description = "${tier * 5}% extra on purchases",
                color = DiamondBlue
            ),
            VipBenefit(
                icon = Icons.Default.Crown,
                title = "Priority Support",
                description = "24/7 VIP customer support",
                color = Color(0xFFFFD700)
            ),
            VipBenefit(
                icon = Icons.Default.LocalOffer,
                title = "Exclusive Offers",
                description = "Access to VIP-only sales",
                color = Color(0xFFFF6B6B)
            ),
            VipBenefit(
                icon = Icons.Default.Groups,
                title = "Family Benefits",
                description = "Bonus for your family members",
                color = AccentGreen
            )
        )
        else -> emptyList()
    }
    
    return baseBenefits + tierBenefits
}

fun getVipColor(tier: Int): Color = when (tier) {
    1 -> Color(0xFFC0C0C0) // Silver
    2 -> Color(0xFFFFD700) // Gold
    3 -> Color(0xFFB19CD9) // Purple
    4 -> Color(0xFF00CED1) // Diamond
    5 -> Color(0xFFFF1493) // Pink Diamond
    else -> PurplePrimary
}

data class VipBenefit(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val color: Color
)
