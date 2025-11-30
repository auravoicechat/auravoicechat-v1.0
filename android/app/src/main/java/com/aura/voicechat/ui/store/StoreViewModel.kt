package com.aura.voicechat.ui.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Store ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class StoreViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()
    
    private var allItems: List<StoreItem> = emptyList()
    
    init {
        loadStoreItems()
    }
    
    private fun loadStoreItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // In production, this would fetch from API
            allItems = getSampleStoreItems()
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                items = allItems,
                featuredItem = allItems.firstOrNull { it.rarity == "legendary" },
                coins = 1500000 // Would come from wallet
            )
        }
    }
    
    fun filterByCategory(category: String) {
        val filtered = if (category == "all") {
            allItems
        } else {
            allItems.filter { it.category == category }
        }
        _uiState.value = _uiState.value.copy(items = filtered)
    }
    
    fun purchaseItem(itemId: String) {
        viewModelScope.launch {
            val item = allItems.find { it.id == itemId } ?: return@launch
            val currentCoins = _uiState.value.coins
            
            if (currentCoins >= item.price) {
                // In production, call API to purchase
                _uiState.value = _uiState.value.copy(
                    coins = currentCoins - item.price,
                    purchaseSuccess = true,
                    purchasedItemId = itemId
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    purchaseError = "Insufficient coins"
                )
            }
        }
    }
    
    fun clearPurchaseState() {
        _uiState.value = _uiState.value.copy(
            purchaseSuccess = false,
            purchaseError = null,
            purchasedItemId = null
        )
    }
    
    private fun getSampleStoreItems(): List<StoreItem> = listOf(
        // Frames
        StoreItem("frame_001", "Golden Crown", "Majestic golden frame", "frames", 5000000, "legendary", duration = "Permanent"),
        StoreItem("frame_002", "Blue Aura", "Cool blue glow", "frames", 50000, "common", duration = "7 days"),
        StoreItem("frame_003", "Fire Ring", "Burning flames", "frames", 200000, "rare", duration = "30 days"),
        StoreItem("frame_004", "Ice Crystal", "Frozen elegance", "frames", 200000, "rare", duration = "30 days"),
        StoreItem("frame_005", "Rainbow Burst", "Colorful spectrum", "frames", 500000, "epic", duration = "30 days"),
        StoreItem("frame_006", "Electric Storm", "Lightning effects", "frames", 800000, "epic", duration = "30 days"),
        
        // Vehicles
        StoreItem("vehicle_001", "Luxury Yacht", "Sail in style", "vehicles", 2000000, "epic", duration = "30 days"),
        StoreItem("vehicle_002", "Romantic Carriage", "Horse-drawn elegance", "vehicles", 5000000, "legendary", duration = "Permanent"),
        StoreItem("vehicle_003", "Sports Car", "Speed and style", "vehicles", 1000000, "epic", duration = "30 days"),
        StoreItem("vehicle_004", "Helicopter", "Sky arrival", "vehicles", 1500000, "epic", duration = "30 days"),
        StoreItem("vehicle_005", "Jet Ski", "Wave rider", "vehicles", 500000, "rare", duration = "14 days"),
        StoreItem("vehicle_006", "Phoenix Rise", "Mythical entrance", "vehicles", 3000000, "legendary", duration = "Permanent"),
        
        // Themes
        StoreItem("theme_001", "Royal Palace", "Gold and purple", "themes", 3000000, "legendary", duration = "30 days"),
        StoreItem("theme_002", "Ocean Dreams", "Blue serenity", "themes", 1500000, "epic", duration = "30 days"),
        StoreItem("theme_003", "Cherry Blossom", "Pink petals", "themes", 800000, "rare", duration = "14 days"),
        StoreItem("theme_004", "Midnight Galaxy", "Stars and cosmos", "themes", 2000000, "epic", duration = "30 days"),
        
        // Mic Skins
        StoreItem("mic_001", "Sound Pulse", "Reactive glow", "mic_skins", 150000, "common", duration = "7 days"),
        StoreItem("mic_002", "Fire Mic", "Flames when speaking", "mic_skins", 600000, "epic", duration = "30 days"),
        StoreItem("mic_003", "Thunder Mic", "Electric effects", "mic_skins", 1200000, "legendary", duration = "30 days"),
        StoreItem("mic_004", "Star Mic", "Sparkling stars", "mic_skins", 400000, "rare", duration = "14 days"),
        
        // Seat Effects
        StoreItem("seat_001", "Starlight", "Twinkling stars", "seat_effects", 100000, "common", duration = "7 days"),
        StoreItem("seat_002", "Bubbles", "Floating bubbles", "seat_effects", 120000, "common", duration = "7 days"),
        StoreItem("seat_003", "Flame Throne", "Royal flames", "seat_effects", 800000, "epic", duration = "30 days"),
        StoreItem("seat_004", "Frost Crown", "Icy elegance", "seat_effects", 800000, "epic", duration = "30 days"),
        
        // Chat Bubbles
        StoreItem("bubble_001", "Classic", "Simple style", "chat_bubbles", 50000, "common", duration = "7 days"),
        StoreItem("bubble_002", "Flame Talk", "Fiery messages", "chat_bubbles", 400000, "epic", duration = "30 days"),
        StoreItem("bubble_003", "Neon Glow", "Bright colors", "chat_bubbles", 250000, "rare", duration = "14 days"),
        StoreItem("bubble_004", "Cosmic", "Galaxy style", "chat_bubbles", 600000, "legendary", duration = "30 days"),
        
        // Entrance Effects
        StoreItem("entrance_001", "Sparkle Entry", "Glitter effect", "entrance", 200000, "rare", duration = "14 days"),
        StoreItem("entrance_002", "Fire Trail", "Burning entrance", "entrance", 500000, "epic", duration = "30 days"),
        StoreItem("entrance_003", "Ice Shatter", "Frozen arrival", "entrance", 500000, "epic", duration = "30 days"),
        StoreItem("entrance_004", "Lightning Strike", "Electric entry", "entrance", 800000, "epic", duration = "30 days"),
        StoreItem("entrance_005", "Petal Storm", "Rose petals", "entrance", 600000, "epic", duration = "30 days")
    )
}

data class StoreUiState(
    val isLoading: Boolean = false,
    val items: List<StoreItem> = emptyList(),
    val featuredItem: StoreItem? = null,
    val coins: Long = 0,
    val purchaseSuccess: Boolean = false,
    val purchaseError: String? = null,
    val purchasedItemId: String? = null
)
