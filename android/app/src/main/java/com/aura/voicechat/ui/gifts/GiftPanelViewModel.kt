package com.aura.voicechat.ui.gifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * Gift data is synced from Owner CMS/database
 * Owner can add/remove/modify gifts via CMS panel
 */
@HiltViewModel
class GiftPanelViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(GiftPanelUiState())
    val uiState: StateFlow<GiftPanelUiState> = _uiState.asStateFlow()
    
    private var allGifts: List<Gift> = emptyList()
    
    fun loadGifts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // In production, this fetches from API which reads from database
            // Owner CMS can add/remove gifts, which updates the database
            // App syncs gift catalog on launch and periodically
            allGifts = getGiftCatalog()
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                gifts = allGifts,
                coins = 5000000 // Would come from wallet repository
            )
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
    
    fun sendGift(recipientId: String, giftId: String, quantity: Int) {
        viewModelScope.launch {
            val gift = allGifts.find { it.id == giftId } ?: return@launch
            val totalCost = gift.price * quantity
            val currentCoins = _uiState.value.coins
            
            if (currentCoins >= totalCost) {
                // In production, call API to send gift
                // API deducts coins, adds diamonds to recipient, triggers animation
                _uiState.value = _uiState.value.copy(
                    coins = currentCoins - totalCost,
                    lastSentGift = gift,
                    lastSentQuantity = quantity
                )
            }
        }
    }
    
    /**
     * Gift catalog - This data comes from database via API
     * Owner CMS has full control to:
     * - Add new gifts
     * - Remove gifts
     * - Modify prices
     * - Enable/disable gifts
     * - Upload animations
     * - Set regional availability
     */
    private fun getGiftCatalog(): List<Gift> = listOf(
        // Love Category - 20 gifts
        Gift("gift_001", "Rose", "love", 50, 50, "common", isAnimated = true),
        Gift("gift_002", "Heart", "love", 100, 100, "common", isAnimated = true),
        Gift("gift_003", "Kiss", "love", 200, 200, "common", isAnimated = true),
        Gift("gift_004", "Hug", "love", 300, 300, "common", isAnimated = true),
        Gift("gift_005", "Love Letter", "love", 500, 500, "common", isAnimated = true),
        Gift("gift_006", "Teddy Bear", "love", 1000, 1000, "common", isAnimated = true),
        Gift("gift_007", "Rose Bouquet", "love", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_008", "Cupid's Arrow", "love", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_009", "Romantic Dinner", "love", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_087", "Emoji Love", "love", 50, 50, "common", isAnimated = true),
        Gift("gift_103", "Red Rose", "love", 100, 100, "common", isAnimated = true),
        Gift("gift_104", "Pink Rose", "love", 100, 100, "common", isAnimated = true),
        Gift("gift_105", "White Rose", "love", 100, 100, "common", isAnimated = true),
        Gift("gift_106", "Love Bird", "love", 1500, 1500, "rare", isAnimated = true),
        Gift("gift_107", "Engagement Ring", "love", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_108", "Wedding", "love", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        
        // Celebration Category - 20 gifts
        Gift("gift_010", "Balloon", "celebration", 50, 50, "common", isAnimated = true),
        Gift("gift_011", "Confetti", "celebration", 100, 100, "common", isAnimated = true),
        Gift("gift_012", "Party Hat", "celebration", 200, 200, "common", isAnimated = true),
        Gift("gift_013", "Cake", "celebration", 500, 500, "common", isAnimated = true),
        Gift("gift_014", "Champagne", "celebration", 1000, 1000, "common", isAnimated = true),
        Gift("gift_015", "Gift Box", "celebration", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_016", "Trophy", "celebration", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_017", "Firework", "celebration", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_092", "Red Envelope", "celebration", 1000, 1000, "common", isAnimated = true),
        Gift("gift_093", "Lantern", "celebration", 800, 800, "common", isAnimated = true),
        Gift("gift_094", "Firework Burst", "celebration", 3000, 3000, "rare", isAnimated = true),
        Gift("gift_095", "Sparkler", "celebration", 500, 500, "common", isAnimated = true),
        Gift("gift_096", "Ribbon", "celebration", 200, 200, "common", isAnimated = true),
        Gift("gift_097", "Medal", "celebration", 1000, 1000, "common", isAnimated = true),
        Gift("gift_112", "Snowman", "celebration", 1000, 1000, "common", isAnimated = true),
        Gift("gift_113", "Christmas Tree", "celebration", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_114", "Santa", "celebration", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_115", "Pumpkin", "celebration", 500, 500, "common", isAnimated = true),
        Gift("gift_116", "Ghost", "celebration", 300, 300, "common", isAnimated = true),
        Gift("gift_117", "Turkey", "celebration", 1000, 1000, "common", isAnimated = true),
        
        // Luxury Category - 15 gifts
        Gift("gift_018", "Diamond Ring", "luxury", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_019", "Gold Bar", "luxury", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_020", "Crown", "luxury", 20000, 20000, "epic", isAnimated = true),
        Gift("gift_021", "Sports Car", "luxury", 50000, 50000, "epic", isAnimated = true),
        Gift("gift_022", "Yacht", "luxury", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_023", "Private Jet", "luxury", 200000, 200000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_024", "Mansion", "luxury", 500000, 500000, "legendary", isAnimated = true, isFullScreen = true),
        Gift("gift_025", "Island", "luxury", 1000000, 1000000, "legendary", isAnimated = true, isFullScreen = true),
        Gift("gift_076", "Treasure Chest", "luxury", 30000, 30000, "epic", isAnimated = true),
        Gift("gift_077", "Money Rain", "luxury", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_078", "Diamond Shower", "luxury", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_098", "Perfume", "luxury", 3000, 3000, "rare", isAnimated = true),
        Gift("gift_099", "Handbag", "luxury", 8000, 8000, "rare", isAnimated = true),
        Gift("gift_100", "Watch", "luxury", 15000, 15000, "epic", isAnimated = true),
        Gift("gift_101", "Necklace", "luxury", 12000, 12000, "epic", isAnimated = true),
        
        // Nature Category - 15 gifts
        Gift("gift_026", "Flower", "nature", 50, 50, "common", isAnimated = true),
        Gift("gift_027", "Butterfly", "nature", 100, 100, "common", isAnimated = true),
        Gift("gift_028", "Rainbow", "nature", 500, 500, "common", isAnimated = true),
        Gift("gift_029", "Sunflower", "nature", 200, 200, "common", isAnimated = true),
        Gift("gift_030", "Cherry Blossom", "nature", 1000, 1000, "rare", isAnimated = true),
        Gift("gift_031", "Ocean Wave", "nature", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_032", "Northern Lights", "nature", 10000, 10000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_083", "Peacock", "nature", 3000, 3000, "rare", isAnimated = true),
        Gift("gift_084", "Swan", "nature", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_085", "Dolphin", "nature", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_086", "Horse", "nature", 8000, 8000, "rare", isAnimated = true),
        Gift("gift_111", "Snowflake", "nature", 200, 200, "common", isAnimated = true),
        
        // Fantasy Category - 15 gifts
        Gift("gift_033", "Unicorn", "fantasy", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_034", "Dragon", "fantasy", 20000, 20000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_035", "Phoenix", "fantasy", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_036", "Magic Wand", "fantasy", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_037", "Fairy", "fantasy", 3000, 3000, "rare", isAnimated = true),
        Gift("gift_038", "Castle", "fantasy", 100000, 100000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_072", "Angel", "fantasy", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_073", "Wings", "fantasy", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_074", "Mermaid", "fantasy", 15000, 15000, "epic", isAnimated = true),
        Gift("gift_075", "Genie Lamp", "fantasy", 20000, 20000, "epic", isAnimated = true),
        
        // Special Category - 25 gifts
        Gift("gift_039", "Star", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_040", "Moon", "special", 500, 500, "common", isAnimated = true),
        Gift("gift_041", "Sun", "special", 1000, 1000, "common", isAnimated = true),
        Gift("gift_042", "Galaxy", "special", 50000, 50000, "epic", isAnimated = true, isFullScreen = true),
        Gift("gift_043", "Coffee", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_044", "Beer", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_045", "Ice Cream", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_046", "Pizza", "special", 200, 200, "common", isAnimated = true),
        Gift("gift_047", "Thumbs Up", "special", 10, 10, "common", isAnimated = true),
        Gift("gift_048", "Applause", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_049", "Fire", "special", 200, 200, "common", isAnimated = true),
        Gift("gift_050", "Lightning", "special", 500, 500, "common", isAnimated = true),
        Gift("gift_063", "Lucky Clover", "special", 300, 300, "common", isAnimated = true),
        Gift("gift_064", "Lollipop", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_065", "Candy", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_066", "Chocolate", "special", 200, 200, "common", isAnimated = true),
        Gift("gift_067", "Microphone", "special", 500, 500, "common", isAnimated = true),
        Gift("gift_068", "Music Note", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_069", "Guitar", "special", 1000, 1000, "common", isAnimated = true),
        Gift("gift_070", "Rocket", "special", 5000, 5000, "rare", isAnimated = true),
        Gift("gift_071", "Spaceship", "special", 10000, 10000, "rare", isAnimated = true),
        Gift("gift_079", "Cat", "special", 200, 200, "common", isAnimated = true),
        Gift("gift_080", "Dog", "special", 200, 200, "common", isAnimated = true),
        Gift("gift_081", "Panda", "special", 500, 500, "common", isAnimated = true),
        Gift("gift_082", "Bunny", "special", 300, 300, "common", isAnimated = true),
        Gift("gift_088", "Emoji Happy", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_089", "Emoji Laugh", "special", 50, 50, "common", isAnimated = true),
        Gift("gift_090", "Emoji Cool", "special", 100, 100, "common", isAnimated = true),
        Gift("gift_091", "Lucky Cat", "special", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_109", "Shooting Star", "special", 2000, 2000, "rare", isAnimated = true),
        Gift("gift_110", "Meteor Shower", "special", 20000, 20000, "epic", isAnimated = true, isFullScreen = true),
        
        // Custom (Aura Exclusive) Category - 8 gifts
        Gift("gift_051", "Aura Rose Garden", "custom", 100000, 100000, "epic", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_052", "Aura Firework Show", "custom", 250000, 250000, "epic", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_053", "Aura Golden Rain", "custom", 500000, 500000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_054", "Aura Heart Explosion", "custom", 500000, 500000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_055", "Aura Northern Lights", "custom", 1000000, 1000000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_056", "Aura Royal Castle", "custom", 2000000, 2000000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_057", "Aura Dragon Dance", "custom", 5000000, 5000000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        Gift("gift_058", "Aura Cosmic Journey", "custom", 10000000, 10000000, "legendary", isAnimated = true, isFullScreen = true, isCustom = true),
        
        // Legendary Category - 4 gifts
        Gift("gift_059", "Universe Creator", "legendary", 50000000, 50000000, "legendary", isAnimated = true, isFullScreen = true, isLegendary = true),
        Gift("gift_060", "Phoenix Rebirth", "legendary", 50000000, 50000000, "legendary", isAnimated = true, isFullScreen = true, isLegendary = true),
        Gift("gift_061", "Love Story", "legendary", 100000000, 100000000, "legendary", isAnimated = true, isFullScreen = true, isLegendary = true),
        Gift("gift_062", "Kingdom Rise", "legendary", 200000000, 200000000, "legendary", isAnimated = true, isFullScreen = true, isLegendary = true)
    )
}

data class GiftPanelUiState(
    val isLoading: Boolean = false,
    val gifts: List<Gift> = emptyList(),
    val coins: Long = 0,
    val lastSentGift: Gift? = null,
    val lastSentQuantity: Int = 0
)
