package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * API Request/Response Models
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// Authentication
data class SendOtpRequest(
    @SerializedName("phone") val phone: String
)

data class SendOtpResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("cooldownSeconds") val cooldownSeconds: Int,
    @SerializedName("attemptsRemaining") val attemptsRemaining: Int
)

data class VerifyOtpRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("otp") val otp: String
)

data class VerifyOtpResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserDto
)

// Daily Rewards
data class DailyRewardStatusResponse(
    @SerializedName("currentDay") val currentDay: Int,
    @SerializedName("claimable") val claimable: Boolean,
    @SerializedName("cycle") val cycle: List<DayRewardDto>,
    @SerializedName("streak") val streak: Int,
    @SerializedName("nextResetUtc") val nextResetUtc: String,
    @SerializedName("vipTier") val vipTier: String,
    @SerializedName("vipMultiplier") val vipMultiplier: Double
)

data class DayRewardDto(
    @SerializedName("day") val day: Int,
    @SerializedName("coins") val coins: Long,
    @SerializedName("bonus") val bonus: Long?,
    @SerializedName("status") val status: String
)

data class ClaimRewardResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("day") val day: Int,
    @SerializedName("baseCoins") val baseCoins: Long,
    @SerializedName("vipMultiplier") val vipMultiplier: Double,
    @SerializedName("totalCoins") val totalCoins: Long
)

// VIP
data class VipTierResponse(
    @SerializedName("tier") val tier: String,
    @SerializedName("multiplier") val multiplier: Double,
    @SerializedName("expBoost") val expBoost: Double,
    @SerializedName("expiry") val expiry: String,
    @SerializedName("benefits") val benefits: List<String>
)

data class PurchaseVipRequest(
    @SerializedName("tier") val tier: String,
    @SerializedName("duration") val duration: String
)

// Medals
data class MedalsResponse(
    @SerializedName("medals") val medals: List<MedalDto>,
    @SerializedName("displaySettings") val displaySettings: DisplaySettingsDto
)

data class MedalDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("earnedAt") val earnedAt: Long?,
    @SerializedName("isDisplayed") val isDisplayed: Boolean
)

data class DisplaySettingsDto(
    @SerializedName("displayedMedals") val displayedMedals: List<String>,
    @SerializedName("hiddenMedals") val hiddenMedals: List<String>,
    @SerializedName("maxDisplayed") val maxDisplayed: Int
)

data class UpdateMedalDisplayRequest(
    @SerializedName("displayedMedals") val displayedMedals: List<String>,
    @SerializedName("hiddenMedals") val hiddenMedals: List<String>
)

// Wallet
data class WalletBalancesResponse(
    @SerializedName("coins") val coins: Long,
    @SerializedName("diamonds") val diamonds: Long,
    @SerializedName("lastUpdated") val lastUpdated: String
)

data class ExchangeRequest(
    @SerializedName("diamonds") val diamonds: Long
)

data class ExchangeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("diamondsUsed") val diamondsUsed: Long,
    @SerializedName("coinsReceived") val coinsReceived: Long,
    @SerializedName("newBalance") val newBalance: NewBalanceDto
)

data class NewBalanceDto(
    @SerializedName("coins") val coins: Long,
    @SerializedName("diamonds") val diamonds: Long
)

// Referrals
data class BindReferralRequest(
    @SerializedName("code") val code: String
)

data class ReferralCoinsSummaryResponse(
    @SerializedName("invitationsCount") val invitationsCount: Int,
    @SerializedName("totalCoinsRewarded") val totalCoinsRewarded: Long,
    @SerializedName("withdrawableCoins") val withdrawableCoins: Long,
    @SerializedName("withdrawMin") val withdrawMin: Long,
    @SerializedName("cooldownSeconds") val cooldownSeconds: Int
)

data class WithdrawCoinsRequest(
    @SerializedName("amount") val amount: Long
)

data class ReferralRecordsResponse(
    @SerializedName("data") val data: List<ReferralRecordDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)

data class ReferralRecordDto(
    @SerializedName("inviteeId") val inviteeId: String,
    @SerializedName("inviteeName") val inviteeName: String,
    @SerializedName("inviteeAvatar") val inviteeAvatar: String?,
    @SerializedName("joinedAt") val joinedAt: Long,
    @SerializedName("coinsRewarded") val coinsRewarded: Long,
    @SerializedName("status") val status: String
)

data class ReferralCashSummaryResponse(
    @SerializedName("balanceUsd") val balanceUsd: Double,
    @SerializedName("minWithdrawalUsd") val minWithdrawalUsd: Double,
    @SerializedName("walletCooldownSeconds") val walletCooldownSeconds: Int,
    @SerializedName("externalAllowedMinUsd") val externalAllowedMinUsd: Double,
    @SerializedName("externalClearanceDays") val externalClearanceDays: Int
)

data class WithdrawCashRequest(
    @SerializedName("destination") val destination: String
)

data class InviteRecordsResponse(
    @SerializedName("data") val data: List<InviteRecordDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)

data class InviteRecordDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("date") val date: String,
    @SerializedName("amount") val amount: Double
)

data class RankingResponse(
    @SerializedName("data") val data: List<RankingEntryDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)

data class RankingEntryDto(
    @SerializedName("rank") val rank: Int,
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("amount") val amount: Double
)

// Rooms
data class RoomsResponse(
    @SerializedName("data") val data: List<RoomCardDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)

data class RoomCardDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("coverImage") val coverImage: String?,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("ownerAvatar") val ownerAvatar: String?,
    @SerializedName("type") val type: String,
    @SerializedName("userCount") val userCount: Int,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("isLive") val isLive: Boolean,
    @SerializedName("tags") val tags: List<String>
)

data class RoomResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("coverImage") val coverImage: String?,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("ownerAvatar") val ownerAvatar: String?,
    @SerializedName("type") val type: String,
    @SerializedName("mode") val mode: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("currentUsers") val currentUsers: Int,
    @SerializedName("isLocked") val isLocked: Boolean,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("seats") val seats: List<SeatDto>,
    @SerializedName("createdAt") val createdAt: Long
)

data class SeatDto(
    @SerializedName("position") val position: Int,
    @SerializedName("userId") val userId: String?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("userAvatar") val userAvatar: String?,
    @SerializedName("userLevel") val userLevel: Int?,
    @SerializedName("userVip") val userVip: Int?,
    @SerializedName("isMuted") val isMuted: Boolean,
    @SerializedName("isLocked") val isLocked: Boolean
)

data class AddToPlaylistRequest(
    @SerializedName("url") val url: String
)

// User
data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("level") val level: Int,
    @SerializedName("vipTier") val vipTier: Int,
    @SerializedName("isNewUser") val isNewUser: Boolean?
)

data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("level") val level: Int,
    @SerializedName("exp") val exp: Long,
    @SerializedName("vipTier") val vipTier: Int,
    @SerializedName("vipExpiry") val vipExpiry: Long?,
    @SerializedName("coins") val coins: Long,
    @SerializedName("diamonds") val diamonds: Long,
    @SerializedName("gender") val gender: String,
    @SerializedName("country") val country: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("isOnline") val isOnline: Boolean,
    @SerializedName("lastActiveAt") val lastActiveAt: Long?,
    @SerializedName("kycStatus") val kycStatus: String,
    @SerializedName("cpPartnerId") val cpPartnerId: String?,
    @SerializedName("familyId") val familyId: String?,
    @SerializedName("createdAt") val createdAt: Long
)

data class UpdateProfileRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("avatar") val avatar: String?
)

// KYC
data class KycStatusResponse(
    @SerializedName("userId") val userId: String,
    @SerializedName("status") val status: String,
    @SerializedName("idCardFront") val idCardFront: String?,
    @SerializedName("idCardBack") val idCardBack: String?,
    @SerializedName("selfie") val selfie: String?,
    @SerializedName("livenessScore") val livenessScore: Float?,
    @SerializedName("submittedAt") val submittedAt: Long?,
    @SerializedName("reviewedAt") val reviewedAt: Long?,
    @SerializedName("rejectionReason") val rejectionReason: String?
)

data class SubmitKycRequest(
    @SerializedName("idCardFrontUri") val idCardFrontUri: String,
    @SerializedName("idCardBackUri") val idCardBackUri: String,
    @SerializedName("selfieUri") val selfieUri: String,
    @SerializedName("livenessCheckPassed") val livenessCheckPassed: Boolean
)

// Common
data class PaginationDto(
    @SerializedName("page") val page: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("totalPages") val totalPages: Int
)
