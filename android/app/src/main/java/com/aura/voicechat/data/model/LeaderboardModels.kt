package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Leaderboard & Rankings Models
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// VIP Tier Enum
enum class VipTier {
    @SerializedName("none") NONE,
    @SerializedName("vip1") VIP1,
    @SerializedName("vip2") VIP2,
    @SerializedName("vip3") VIP3,
    @SerializedName("vip4") VIP4,
    @SerializedName("vip5") VIP5,
    @SerializedName("svip") SVIP
}

// Ranking User Data Model
data class RankingUser(
    @SerializedName("rank") val rank: Int,
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("level") val level: Int,
    @SerializedName("vipTier") val vipTier: VipTier,
    @SerializedName("score") val score: Long,
    @SerializedName("changeFromPrevious") val changeFromPrevious: Int // positive = up, negative = down
)

// API Response for leaderboard
data class LeaderboardResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("users") val users: List<RankingUser>,
    @SerializedName("myRank") val myRank: RankingUser?
)
