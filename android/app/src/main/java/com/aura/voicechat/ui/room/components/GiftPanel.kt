package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aura.voicechat.domain.model.Gift
import com.aura.voicechat.domain.model.GiftCategory
import com.aura.voicechat.ui.theme.*

/**
 * GiftPanel Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Bottom sheet for gift selection with category tabs, gift grid, and quantity selector
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftPanel(
    gifts: List<Gift>,
    onDismiss: () -> Unit,
    onSendGift: (Gift, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(GiftCategory.LOVE) }
    var selectedGift by remember { mutableStateOf<Gift?>(null) }
    var selectedQuantity by remember { mutableStateOf(1) }
    
    val categories = listOf(
        GiftCategory.LOVE,
        GiftCategory.CELEBRATION,
        GiftCategory.LUXURY,
        GiftCategory.NATURE,
        GiftCategory.FANTASY,
        GiftCategory.SPECIAL
    )
    
    val quantities = listOf(1, 10, 99, 520, 1314)
    
    val filteredGifts = gifts.filter { it.category == selectedCategory }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Send Gift",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Category tabs
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                containerColor = Color.Transparent,
                contentColor = AccentMagenta,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        text = {
                            Text(
                                text = category.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Gift grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                items(filteredGifts) { gift ->
                    GiftItem(
                        gift = gift,
                        isSelected = gift == selectedGift,
                        onClick = { selectedGift = gift }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quantity selector
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quantities.forEach { quantity ->
                    FilterChip(
                        selected = quantity == selectedQuantity,
                        onClick = { selectedQuantity = quantity },
                        label = { Text(quantity.toString()) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCard,
                            selectedContainerColor = AccentMagenta,
                            labelColor = TextPrimary,
                            selectedLabelColor = TextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Send button
            Button(
                onClick = {
                    selectedGift?.let { gift ->
                        onSendGift(gift, selectedQuantity)
                        onDismiss()
                    }
                },
                enabled = selectedGift != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentMagenta,
                    disabledContainerColor = DarkCard
                )
            ) {
                Text(
                    text = if (selectedGift != null) {
                        "Send for ${selectedGift!!.price * selectedQuantity} coins"
                    } else {
                        "Select a gift"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun GiftItem(
    gift: Gift,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, AccentMagenta, RoundedCornerShape(8.dp))
                } else {
                    Modifier
                }
            )
            .background(DarkCard, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gift icon placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Purple40.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CardGiftcard,
                contentDescription = gift.name,
                tint = Purple80,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Gift name
        Text(
            text = gift.name,
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary,
            maxLines = 1
        )
        
        // Gift price
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(10.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = gift.price.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = AccentGold,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
