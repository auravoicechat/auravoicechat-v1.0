package com.aura.voicechat.ui.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.model.ClaimRewardResult
import com.aura.voicechat.domain.model.DailyRewardSchedule
import com.aura.voicechat.domain.model.DayReward
import com.aura.voicechat.domain.model.RewardStatus
import com.aura.voicechat.domain.model.VipTiers
import com.aura.voicechat.domain.repository.RewardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Daily Rewards ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * 7-day cycle: Day 1 (5K) → Day 7 (50K)
 * VIP multipliers: 1.2x (VIP1) → 3.0x (VIP10)
 */
@HiltViewModel
class DailyRewardsViewModel @Inject constructor(
    private val rewardsRepository: RewardsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DailyRewardsUiState())
    val uiState: StateFlow<DailyRewardsUiState> = _uiState.asStateFlow()
    
    init {
        loadRewardStatus()
    }
    
    private fun loadRewardStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Mock data - in real app, fetch from repository
                val currentDay = 6
                val streak = 5
                val vipTier = 5
                val vipMultiplier = VipTiers.getMultiplier(vipTier)
                
                val cycle = DailyRewardSchedule.rewards.mapIndexed { index, reward ->
                    reward.copy(
                        status = when {
                            index < currentDay - 1 -> RewardStatus.CLAIMED
                            index == currentDay - 1 -> RewardStatus.CLAIMABLE
                            else -> RewardStatus.LOCKED
                        }
                    )
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentDay = currentDay,
                    claimable = true,
                    cycle = cycle,
                    streak = streak,
                    vipTier = vipTier,
                    vipMultiplier = vipMultiplier
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun claimReward() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Simulate API call
                delay(1000)
                
                val currentDay = _uiState.value.currentDay
                val baseCoins = DailyRewardSchedule.getReward(currentDay)
                val vipMultiplier = _uiState.value.vipMultiplier
                val totalCoins = (baseCoins * vipMultiplier).toLong()
                
                val result = ClaimRewardResult(
                    success = true,
                    day = currentDay,
                    baseCoins = baseCoins,
                    vipMultiplier = vipMultiplier,
                    totalCoins = totalCoins
                )
                
                // Update cycle to show claimed
                val updatedCycle = _uiState.value.cycle.map { dayReward ->
                    if (dayReward.day == currentDay) {
                        dayReward.copy(status = RewardStatus.CLAIMED)
                    } else if (dayReward.day == currentDay + 1 && currentDay < 7) {
                        dayReward.copy(status = RewardStatus.CLAIMABLE)
                    } else {
                        dayReward
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    claimable = false,
                    cycle = updatedCycle,
                    streak = _uiState.value.streak + 1,
                    currentDay = if (currentDay < 7) currentDay + 1 else 1,
                    claimResult = result
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun clearClaimResult() {
        _uiState.value = _uiState.value.copy(claimResult = null)
    }
}

data class DailyRewardsUiState(
    val isLoading: Boolean = false,
    val currentDay: Int = 1,
    val claimable: Boolean = false,
    val cycle: List<DayReward> = emptyList(),
    val streak: Int = 0,
    val vipTier: Int = 0,
    val vipMultiplier: Double = 1.0,
    val claimResult: ClaimRewardResult? = null,
    val error: String? = null
)
