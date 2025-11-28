package com.aura.voicechat.domain.model

/**
 * Daily Reward domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * 7-day cycle: Day 1 (5K) → Day 7 (50K)
 * VIP multipliers applied to rewards
 */
data class DailyRewardStatus(
    val currentDay: Int,
    val claimable: Boolean,
    val cycle: List<DayReward>,
    val streak: Int,
    val nextResetUtc: String,
    val vipTier: Int,
    val vipMultiplier: Double
)

data class DayReward(
    val day: Int,
    val coins: Long,
    val bonus: Long?,
    val status: RewardStatus
)

enum class RewardStatus {
    CLAIMED,
    CLAIMABLE,
    LOCKED
}

data class ClaimRewardResult(
    val success: Boolean,
    val day: Int,
    val baseCoins: Long,
    val vipMultiplier: Double,
    val totalCoins: Long
)

/**
 * Daily reward schedule as per documentation:
 * Day 1: 5,000 coins
 * Day 2: 10,000 coins
 * Day 3: 15,000 coins
 * Day 4: 20,000 coins
 * Day 5: 25,000 coins
 * Day 6: 30,000 coins
 * Day 7: 35,000 + 15,000 bonus = 50,000 coins
 */
object DailyRewardSchedule {
    val rewards = listOf(
        DayReward(1, 5_000, null, RewardStatus.LOCKED),
        DayReward(2, 10_000, null, RewardStatus.LOCKED),
        DayReward(3, 15_000, null, RewardStatus.LOCKED),
        DayReward(4, 20_000, null, RewardStatus.LOCKED),
        DayReward(5, 25_000, null, RewardStatus.LOCKED),
        DayReward(6, 30_000, null, RewardStatus.LOCKED),
        DayReward(7, 35_000, 15_000, RewardStatus.LOCKED) // Total: 50,000
    )
    
    fun getReward(day: Int): Long {
        val reward = rewards.find { it.day == day } ?: return 0
        return reward.coins + (reward.bonus ?: 0)
    }
    
    fun getRewardWithMultiplier(day: Int, vipTier: Int): Long {
        val baseReward = getReward(day)
        val multiplier = VipTiers.getMultiplier(vipTier)
        return (baseReward * multiplier).toLong()
    }
}
