package com.aura.voicechat.domain.repository

import com.aura.voicechat.domain.model.ClaimRewardResult
import com.aura.voicechat.domain.model.DailyRewardStatus

/**
 * Rewards Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface RewardsRepository {
    suspend fun getDailyRewardStatus(): Result<DailyRewardStatus>
    suspend fun claimDailyReward(): Result<ClaimRewardResult>
}
