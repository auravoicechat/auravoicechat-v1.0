package com.aura.voicechat.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Inventory ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class InventoryViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()
    
    private var allItems: List<InventoryItem> = emptyList()
    
    init {
        loadInventory()
    }
    
    private fun loadInventory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // In production, this would fetch from API
            allItems = getSampleInventory()
            val equipped = getSampleEquipped()
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                items = allItems,
                equippedItems = equipped
            )
        }
    }
    
    fun filterByCategory(category: String) {
        val filtered = when (category) {
            "all" -> allItems
            "baggage" -> allItems.filter { it.isBaggage }
            else -> allItems.filter { it.category == category }
        }
        _uiState.value = _uiState.value.copy(items = filtered)
    }
    
    fun selectItem(itemId: String) {
        val item = allItems.find { it.id == itemId }
        _uiState.value = _uiState.value.copy(selectedItem = item)
    }
    
    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedItem = null)
    }
    
    fun equipItem(itemId: String) {
        val item = allItems.find { it.id == itemId } ?: return
        val currentEquipped = _uiState.value.equippedItems.toMutableList()
        
        // Remove any existing item in same category
        currentEquipped.removeAll { it.category == item.category }
        currentEquipped.add(item)
        
        _uiState.value = _uiState.value.copy(
            equippedItems = currentEquipped,
            selectedItem = null
        )
    }
    
    fun unequipItem(itemId: String) {
        val currentEquipped = _uiState.value.equippedItems.toMutableList()
        currentEquipped.removeAll { it.id == itemId }
        
        _uiState.value = _uiState.value.copy(
            equippedItems = currentEquipped,
            selectedItem = null
        )
    }
    
    private fun getSampleInventory(): List<InventoryItem> = listOf(
        // Owned Frames
        InventoryItem("frame_owned_001", "Blue Aura", "Cool blue glow frame", "frames", "common", expiresIn = "5 days"),
        InventoryItem("frame_owned_002", "Fire Ring", "Burning flames border", "frames", "rare", expiresIn = "25 days"),
        InventoryItem("frame_owned_003", "Golden Crown", "Majestic royal frame", "frames", "legendary"),
        
        // Owned Vehicles
        InventoryItem("vehicle_owned_001", "Sports Car", "Red luxury sports car", "vehicles", "epic", expiresIn = "18 days"),
        InventoryItem("vehicle_owned_002", "Jet Ski", "Wave riding machine", "vehicles", "rare", expiresIn = "8 days"),
        
        // Owned Themes
        InventoryItem("theme_owned_001", "Cherry Blossom", "Pink sakura theme", "themes", "rare", expiresIn = "10 days"),
        
        // Owned Mic Skins
        InventoryItem("mic_owned_001", "Star Mic", "Sparkling star effects", "mic_skins", "rare", expiresIn = "12 days"),
        
        // Owned Seat Effects
        InventoryItem("seat_owned_001", "Bubbles", "Floating bubble effects", "seat_effects", "common", expiresIn = "3 days", isExpiringSoon = true),
        InventoryItem("seat_owned_002", "Flame Throne", "Royal fire effects", "seat_effects", "epic", expiresIn = "20 days"),
        
        // Owned Chat Bubbles
        InventoryItem("bubble_owned_001", "Neon Glow", "Bright neon style", "chat_bubbles", "rare", expiresIn = "7 days"),
        
        // Baggage Items (free gifts)
        InventoryItem("baggage_001", "Event Rose", "Special event gift", "baggage", "rare", isBaggage = true),
        InventoryItem("baggage_002", "Birthday Cake", "Anniversary reward", "baggage", "epic", isBaggage = true),
        InventoryItem("baggage_003", "Lucky Star", "Login bonus gift", "baggage", "common", isBaggage = true),
        InventoryItem("baggage_004", "CP Heart", "Partnership reward", "baggage", "epic", isBaggage = true),
        InventoryItem("baggage_005", "Friend Medal", "Friendship milestone", "baggage", "rare", isBaggage = true)
    )
    
    private fun getSampleEquipped(): List<InventoryItem> = listOf(
        InventoryItem("frame_owned_003", "Golden Crown", "Majestic royal frame", "frames", "legendary"),
        InventoryItem("vehicle_owned_001", "Sports Car", "Red luxury sports car", "vehicles", "epic", expiresIn = "18 days"),
        InventoryItem("mic_owned_001", "Star Mic", "Sparkling star effects", "mic_skins", "rare", expiresIn = "12 days")
    )
}

data class InventoryUiState(
    val isLoading: Boolean = false,
    val items: List<InventoryItem> = emptyList(),
    val equippedItems: List<InventoryItem> = emptyList(),
    val selectedItem: InventoryItem? = null
)
