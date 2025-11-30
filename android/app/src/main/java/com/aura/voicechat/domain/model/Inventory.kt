package com.aura.voicechat.domain.model

/**
 * Inventory Item Domain Model
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Represents an item owned by a user
 */
data class InventoryItem(
    val id: String,
    val itemId: String, // Reference to store item
    val name: String,
    val description: String,
    val category: ItemCategory,
    val rarity: ItemRarity,
    val iconUrl: String? = null,
    val animationUrl: String? = null,
    val acquiredAt: Long,
    val expiresAt: Long? = null, // null = permanent
    val isEquipped: Boolean = false,
    val source: ItemSource
)

enum class ItemCategory {
    FRAME,
    VEHICLE,
    THEME,
    MIC_SKIN,
    SEAT_EFFECT,
    CHAT_BUBBLE,
    ENTRANCE_STYLE,
    ROOM_CARD,
    COVER,
    BAGGAGE // Free gifts from events/rewards
}

enum class ItemRarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY
}

enum class ItemSource {
    STORE_PURCHASE,
    VIP_REWARD,
    CP_REWARD,
    FRIEND_REWARD,
    EVENT_REWARD,
    LEVEL_REWARD,
    GIFT,
    ADMIN_GRANT
}

/**
 * Store Item - Available for purchase
 */
data class StoreItem(
    val id: String,
    val name: String,
    val description: String,
    val category: ItemCategory,
    val rarity: ItemRarity,
    val price: Long,
    val iconUrl: String? = null,
    val animationUrl: String? = null,
    val previewUrl: String? = null,
    val duration: ItemDuration,
    val vipRequired: Int = 0, // 0 = no VIP required
    val enabled: Boolean = true,
    val isFeatured: Boolean = false,
    val isNew: Boolean = false,
    val discount: Int = 0 // Percentage discount
)

enum class ItemDuration {
    DAYS_7,
    DAYS_14,
    DAYS_30,
    DAYS_90,
    PERMANENT
}

/**
 * Equipped Items - Currently active cosmetics
 */
data class EquippedItems(
    val frame: InventoryItem? = null,
    val vehicle: InventoryItem? = null,
    val theme: InventoryItem? = null,
    val micSkin: InventoryItem? = null,
    val seatEffect: InventoryItem? = null,
    val chatBubble: InventoryItem? = null,
    val entranceStyle: InventoryItem? = null,
    val roomCard: InventoryItem? = null,
    val cover: InventoryItem? = null
)

/**
 * Baggage Item - Free gift that can be sent
 */
data class BaggageItem(
    val id: String,
    val giftId: String,
    val giftName: String,
    val giftIconUrl: String? = null,
    val diamondValue: Long,
    val quantity: Int,
    val source: ItemSource,
    val acquiredAt: Long,
    val expiresAt: Long? = null
)
