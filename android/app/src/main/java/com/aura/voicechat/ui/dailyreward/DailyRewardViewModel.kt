package com.aura.voicechat.ui.dailyreward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Daily Reward ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class DailyRewardViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(DailyRewardUiState())
    val uiState: StateFlow<DailyRewardUiState> = _uiState.asStateFlow()
    
    init {
        loadRewards()
    }
    
    private fun loadRewards() {
        viewModelScope.launch {
            _uiState.value = DailyRewardUiState(
                currentDay = 4,
                currentStreak = 4,
                longestStreak = 12,
                canClaimToday = true,
                rewards = listOf(
                    DailyReward(1, "coins", 5000, "5K", isClaimed = true),
                    DailyReward(2, "coins", 10000, "10K", isClaimed = true),
                    DailyReward(3, "coins", 15000, "15K", isClaimed = true),
                    DailyReward(4, "coins", 25000, "25K"),
                    DailyReward(5, "diamonds", 100, "100💎"),
                    DailyReward(6, "coins", 50000, "50K"),
                    DailyReward(7, "coins", 100000, "100K", bonusItem = "Bronze Frame (3d)")
                )
            )
        }
    }
    
    fun claimReward() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updatedRewards = currentState.rewards.map { reward ->
                if (reward.day == currentState.currentDay) {
                    reward.copy(isClaimed = true)
                } else {
                    reward
                }
            }
            
            _uiState.value = currentState.copy(
                rewards = updatedRewards,
                canClaimToday = false,
                currentStreak = currentState.currentStreak
            )
        }
    }
}

data class DailyRewardUiState(
    val isLoading: Boolean = false,
    val currentDay: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val canClaimToday: Boolean = false,
    val rewards: List<DailyReward> = emptyList()
)
