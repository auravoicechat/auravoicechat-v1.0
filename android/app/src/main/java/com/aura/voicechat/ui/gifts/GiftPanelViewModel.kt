package com.aura.voicechat.ui.gifts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.SendGiftRequest
import com.aura.voicechat.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Gift Panel ViewModel - Manages gift catalog and sending
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Gift data is synced from backend database via API.
 * Owner can add/remove/modify gifts via CMS panel.
 */
@HiltViewModel
class GiftPanelViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    
    companion object {
        private const val TAG = "GiftPanelViewModel"
    }
    
    private val _uiState = MutableStateFlow(GiftPanelUiState())
    val uiState: StateFlow<GiftPanelUiState> = _uiState.asStateFlow()
    
    private var allGifts: List<Gift> = emptyList()
    
    fun loadGifts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Fetch gifts from backend API
                val response = apiService.getGifts()
                if (response.isSuccessful) {
                    val data = response.body()
                    allGifts = data?.gifts?.map { dto ->
                        Gift(
                            id = dto.id,
                            name = dto.name,
                            category = dto.category,
                            price = dto.priceCoins,
                            diamondValue = dto.diamondValue,
                            rarity = when {
                                dto.priceCoins >= 50000000 -> "legendary"
                                dto.priceCoins >= 10000 -> "epic"
                                dto.priceCoins >= 1000 -> "rare"
                                else -> "common"
                            },
                            iconUrl = dto.thumbnailUrl,
                            animationFile = dto.animationUrl,
                            isAnimated = dto.animationUrl != null,
                            isFullScreen = dto.priceCoins >= 50000,
                            isCustom = dto.category == "custom",
                            isLegendary = dto.priceCoins >= 50000000,
                            isPremium = dto.isPremium
                        )
                    } ?: emptyList()
                    
                    Log.d(TAG, "Loaded ${allGifts.size} gifts from API")
                } else {
                    Log.e(TAG, "Failed to load gifts: ${response.code()}")
                    // Fall back to mock data
                    allGifts = getGiftCatalog()
                }
                
                // Also fetch wallet balance
                try {
                    val walletResponse = apiService.getWalletBalances()
                    val coins = if (walletResponse.isSuccessful) {
                        walletResponse.body()?.coins ?: 0
                    } else 0
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        gifts = allGifts,
                        coins = coins
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        gifts = allGifts,
                        coins = 0
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading gifts", e)
                allGifts = getGiftCatalog()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    gifts = allGifts,
                    coins = 0
                )
            }
        }
    }
    
    fun filterByCategory(category: String) {
        val filtered = if (category == "all") {
            allGifts
        } else {
            allGifts.filter { it.category == category }
        }
        _uiState.value = _uiState.value.copy(gifts = filtered)
    }
    
    fun sendGift(recipientId: String, giftId: String, quantity: Int, roomId: String? = null) {
        viewModelScope.launch {
            val gift = allGifts.find { it.id == giftId } ?: return@launch
            val totalCost = gift.price * quantity
            val currentCoins = _uiState.value.coins
            
            if (currentCoins >= totalCost) {
                try {
                    val response = apiService.sendGift(
                        SendGiftRequest(
                            giftId = giftId,
                            receiverId = recipientId,
                            roomId = roomId,
                            quantity = quantity
                        )
                    )
                    
                    if (response.isSuccessful) {
                        val data = response.body()
                        _uiState.value = _uiState.value.copy(
                            coins = currentCoins - totalCost,
                            lastSentGift = gift,
                            lastSentQuantity = quantity
                        )
                        Log.d(TAG, "Gift sent successfully: $giftId x $quantity")
                    } else {
                        Log.e(TAG, "Failed to send gift: ${response.code()}")
                        _uiState.value = _uiState.value.copy(
                            error = "Failed to send gift. Please try again."
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error sending gift", e)
                    // Show error instead of optimistic update to prevent inconsistent state
                    _uiState.value = _uiState.value.copy(
                        error = "Network error. Please check your connection."
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "Insufficient coins"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Gift catalog fallback - Used when API fails
     * This data comes from database via API in production.
     */
    private fun getGiftCatalog(): List<Gift> = listOf(
        // Love Category
        Gift("gift_001", "Rose", "love", 50, 50, "common", isAnimated = true),
        Gift("gift_002", "Heart", "love", 100, 100, "common", isAnimated = true),
        Gift("gift_003", "Kiss", "love", 200, 200, "common", isAnimated = true),
        Gift("gift_004", "Hug", "love", 300, 300, "common", isAnimated = true),
        Gift("gift_005", "Love Letter", "love", 500, 500, "common", isAnimated = true),
        Gift("gift_006", "Teddy Bear", "love", 1000, 1000, "common", isAnimated = true),
        Gift("gift_007", "Rose Bouquet", "love", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_008", "Cupid's Arrow", "love", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_009", "Romantic Dinner", "love", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_107", "Engagement Ring", "love", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_108", "Wedding", "love", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        
        // Celebration Category
        Gift("gift_010", "Balloon", "celebration", 50, 50, "common", isAnimated = true),
        Gift("gift_011", "Confetti", "celebration", 100, 100, "common", isAnimated = true),
        Gift("gift_012", "Party Hat", "celebration", 200, 200, "common", isAnimated = true),
        Gift("gift_013", "Cake", "celebration", 500, 500, "common", isAnimated = true),
        Gift("gift_014", "Champagne", "celebration", 1000, 1000, "common", isAnimated = true),
        Gift("gift_015", "Gift Box", "celebration", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_016", "Trophy", "celebration", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_017", "Firework", "celebration", 10000, 10000, "rare", isAnimated = true),
        
        // Luxury Category
        Gift("gift_018", "Diamond Ring", "luxury", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_019", "Gold Bar", "luxury", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_020", "Crown", "luxury", 20000, 20000, "epic", isAnimated = true),
        Gift("gift_021", "Sports Car", "luxury", 50000, 50000, "epic", isAnimated = true),
        Gift("gift_022", "Yacht", "luxury", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_023", "Private Jet", "luxury", 200000, 200000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_024", "Mansion", "luxury", 500000, 500000, "legendary", isAnimated = true, isFullScreen = true),
        Gift("gift_025", "Island", "luxury", 1000000, 1000000, "legendary", isAnimated = true, isFullScreen = true),
        
        // Nature Category
        Gift("gift_026", "Flower", "nature", 50, 50, "common", isAnimated = true),
        Gift("gift_027", "Butterfly", "nature", 100, 100, "common", isAnimated = true),
        Gift("gift_028", "Rainbow", "nature", 500, 500, "common", isAnimated = true),
        Gift("gift_030", "Cherry Blossom", "nature", 1000, 1000, "rare", isAnimated = true),
        Gift("gift_032", "Northern Lights", "nature", 10000, 10000, "epic", isAnimated = true, isFullScreen = true),
        
        // Fantasy Category
        Gift("gift_033", "Unicorn", "fantasy", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_034", "Dragon", "fantasy", 20000, 20000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_035", "Phoenix", "fantasy", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_036", "Magic Wand", "fantasy", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_038", "Castle", "fantasy", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        
        // Special Category
        Gift("gift_039", "Star", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_042", "Galaxy", "special", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_043", "Coffee", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_047", "Thumbs Up", "special", 10, 10, "common", isAnimated = true),
        Gift("gift_048", "Applause", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_070", "Rocket", "special", 5000, 5000, "rare", isAnimated = true),
        
        // Custom (Aura Exclusive) Category
        Gift("gift_051", "Aura Rose Garden", "custom", 100000, 100000, "epic", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_053", "Aura Golden Rain", "custom", 500000, 500000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_055", "Aura Northern Lights", "custom", 1000000, 1000000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        
        // Legendary Category
        Gift("gift_059", "Universe Creator", "legendary", 50000000, 50000000, "legendary", isAnimated = true, isFullScreen = true, isLegendary = true),
        Gift("gift_061", "Love Story", "legendary", 100000000, 100000000, "legendary", isAnimated = true, isFullScreen = true, isLegendary = true)
    )
}

data class GiftPanelUiState(
    val isLoading: Boolean = false,
    val gifts: List<Gift> = emptyList(),
    val coins: Long = 0,
    val lastSentGift: Gift? = null,
    val lastSentQuantity: Int = 0,
    val error: String? = null
)
