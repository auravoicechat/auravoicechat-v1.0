package com.aura.voicechat.domain.model

/**
 * Wallet domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Coins and Diamonds, exchange rate 30%
 */
data class Wallet(
    val coins: Long,
    val diamonds: Long,
    val lastUpdated: Long
)

data class ExchangeResult(
    val success: Boolean,
    val diamondsUsed: Long,
    val coinsReceived: Long,
    val newBalance: Wallet
)

data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Long,
    val currency: Currency,
    val description: String,
    val timestamp: Long,
    val relatedUserId: String?,
    val relatedUserName: String?
)

enum class TransactionType {
    GIFT_SENT,
    GIFT_RECEIVED,
    DAILY_REWARD,
    VIP_PURCHASE,
    STORE_PURCHASE,
    EXCHANGE,
    TRANSFER_IN,
    TRANSFER_OUT,
    REFERRAL_BONUS,
    EVENT_REWARD
}

enum class Currency {
    COINS,
    DIAMONDS,
    USD
}

/**
 * Exchange rate: 100,000 diamonds → 30,000 coins (30% rate)
 */
object ExchangeRates {
    const val DIAMOND_TO_COIN_RATE = 0.30
    
    fun calculateCoins(diamonds: Long): Long {
        return (diamonds * DIAMOND_TO_COIN_RATE).toLong()
    }
}
