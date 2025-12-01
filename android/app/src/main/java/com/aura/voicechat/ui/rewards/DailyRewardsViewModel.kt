package com.aura.voicechat.ui.rewards

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.model.ClaimRewardResult
import com.aura.voicechat.domain.model.DailyRewardSchedule
import com.aura.voicechat.domain.model.DayReward
import com.aura.voicechat.domain.model.RewardStatus
import com.aura.voicechat.domain.model.VipTiers
import com.aura.voicechat.domain.repository.RewardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Daily Rewards ViewModel (Live API Connected)
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * 7-day cycle: Day 1 (5K) → Day 7 (50K)
 * VIP multipliers: 1.2x (VIP1) → 3.0x (VIP10)
 */
@HiltViewModel
class DailyRewardsViewModel @Inject constructor(
    private val rewardsRepository: RewardsRepository,
    private val apiService: ApiService
) : ViewModel() {
    
    companion object {
        private const val TAG = "DailyRewardsViewModel"
    }
    
    private val _uiState = MutableStateFlow(DailyRewardsUiState())
    val uiState: StateFlow<DailyRewardsUiState> = _uiState.asStateFlow()
    
    init {
        loadRewardStatus()
    }
    
    private fun loadRewardStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = apiService.getDailyRewardStatus()
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    val vipMultiplier = VipTiers.getMultiplier(data.vipTier)
                    
                    val cycle = DailyRewardSchedule.rewards.mapIndexed { index, reward ->
                        reward.copy(
                            status = when {
                                index < data.currentDay - 1 -> RewardStatus.CLAIMED
                                index == data.currentDay - 1 && data.canClaim -> RewardStatus.CLAIMABLE
                                index == data.currentDay - 1 && !data.canClaim -> RewardStatus.CLAIMED
                                else -> RewardStatus.LOCKED
                            }
                        )
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentDay = data.currentDay,
                        claimable = data.canClaim,
                        cycle = cycle,
                        streak = data.streak,
                        vipTier = data.vipTier,
                        vipMultiplier = vipMultiplier
                    )
                    Log.d(TAG, "Loaded daily reward status: day=${data.currentDay}, canClaim=${data.canClaim}")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load reward status"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading reward status", e)
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
                val response = apiService.claimDailyReward()
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    
                    val result = ClaimRewardResult(
                        success = true,
                        day = _uiState.value.currentDay,
                        baseCoins = data.baseCoins,
                        vipMultiplier = _uiState.value.vipMultiplier,
                        totalCoins = data.totalCoins
                    )
                    
                    // Update cycle to show claimed
                    val currentDay = _uiState.value.currentDay
                    val updatedCycle = _uiState.value.cycle.map { dayReward ->
                        if (dayReward.day == currentDay) {
                            dayReward.copy(status = RewardStatus.CLAIMED)
                        } else if (dayReward.day == currentDay + 1 && currentDay < 7) {
                            dayReward.copy(status = RewardStatus.LOCKED) // Tomorrow
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
                    Log.d(TAG, "Claimed daily reward: ${data.totalCoins} coins")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to claim reward"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error claiming reward", e)
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
    
    fun refresh() {
        loadRewardStatus()
    }
    
    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
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
