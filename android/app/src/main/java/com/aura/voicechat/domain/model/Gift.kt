package com.aura.voicechat.domain.model

/**
 * Gift Domain Model
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Represents a gift item that can be sent in rooms
 * Gift catalog is managed by Owner CMS
 */
data class Gift(
    val id: String,
    val name: String,
    val description: String,
    val category: GiftCategory,
    val price: Long,
    val diamondValue: Long,
    val rarity: GiftRarity,
    val iconUrl: String? = null,
    val animationFile: String? = null,
    val soundFile: String? = null,
    val isAnimated: Boolean = false,
    val isFullScreen: Boolean = false,
    val isCustom: Boolean = false,
    val isLegendary: Boolean = false,
    val duration: Int = 2000, // Animation duration in ms
    val enabled: Boolean = true,
    val regions: List<String> = listOf("all")
)

enum class GiftCategory {
    LOVE,
    CELEBRATION,
    LUXURY,
    NATURE,
    FANTASY,
    SPECIAL,
    CUSTOM,
    LEGENDARY
}

enum class GiftRarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY
}

/**
 * Gift Send Request
 */
data class GiftSendRequest(
    val giftId: String,
    val recipientIds: List<String>,
    val quantity: Int,
    val roomId: String? = null
)

/**
 * Gift Send Result
 */
data class GiftSendResult(
    val success: Boolean,
    val giftId: String,
    val quantity: Int,
    val totalCoinsSpent: Long,
    val recipientsDiamondsReceived: Long,
    val animationUrl: String? = null,
    val errorMessage: String? = null
)

/**
 * Gift Transaction Record
 */
data class GiftTransaction(
    val id: String,
    val giftId: String,
    val giftName: String,
    val senderId: String,
    val senderName: String,
    val recipientId: String,
    val recipientName: String,
    val quantity: Int,
    val coinsSpent: Long,
    val diamondsReceived: Long,
    val roomId: String?,
    val timestamp: Long
)
