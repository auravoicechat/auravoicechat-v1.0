package com.aura.voicechat.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aura.voicechat.domain.model.RoomCard
import com.aura.voicechat.domain.model.RoomCategory
import com.aura.voicechat.ui.theme.AccentMagenta
import com.aura.voicechat.ui.theme.DarkCard
import com.aura.voicechat.ui.theme.TextPrimary

/**
 * PopularTab Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Popular tab with horizontal scrollable category chips and lazy grid of room cards
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularTab(
    rooms: List<RoomCard>,
    selectedCategory: RoomCategory = RoomCategory.ALL,
    onCategorySelected: (RoomCategory) -> Unit,
    onRoomClick: (String) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean = false,
    modifier: Modifier = Modifier
) {
    val categories = RoomCategory.values().toList()
    
    Column(modifier = modifier.fillMaxSize()) {
        // Category chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
        
        // Room grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(rooms) { room ->
                RoomCard(
                    room = room,
                    onClick = { onRoomClick(room.id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: RoomCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = category.displayName,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = DarkCard,
            selectedContainerColor = AccentMagenta,
            labelColor = TextPrimary,
            selectedLabelColor = TextPrimary
        )
    )
}
