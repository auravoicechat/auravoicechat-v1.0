package com.aura.voicechat.domain.model

/**
 * Referral program domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Get Coins and Get Cash programs
 */
data class ReferralSummary(
    val invitationCode: String,
    val invitationsCount: Int,
    val totalCoinsRewarded: Long,
    val withdrawableCoins: Long,
    val withdrawMin: Long,
    val cooldownSeconds: Int
)

data class CashReferralSummary(
    val balanceUsd: Double,
    val minWithdrawalUsd: Double,
    val walletCooldownSeconds: Int,
    val externalAllowedMinUsd: Double,
    val externalClearanceDays: Int,
    val levels: List<CashReferralLevel>
)

data class CashReferralLevel(
    val level: Int,
    val invitesRequired: Int,
    val rewardUsd: Double
)

data class ReferralRecord(
    val inviteeId: String,
    val inviteeName: String,
    val inviteeAvatar: String?,
    val joinedAt: Long,
    val coinsRewarded: Long,
    val status: ReferralStatus
)

enum class ReferralStatus {
    PENDING,
    ACTIVE,
    REWARDED,
    EXPIRED
}

data class WithdrawDestination(
    val type: WithdrawType,
    val details: String?
)

enum class WithdrawType {
    WALLET,
    BANK,
    CARD,
    PAYPAL,
    PAYONEER
}
