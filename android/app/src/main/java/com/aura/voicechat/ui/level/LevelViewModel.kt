package com.aura.voicechat.ui.level

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Level ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class LevelViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(LevelUiState())
    val uiState: StateFlow<LevelUiState> = _uiState.asStateFlow()
    
    init {
        loadLevelData()
    }
    
    private fun loadLevelData() {
        viewModelScope.launch {
            _uiState.value = LevelUiState(
                currentLevel = 15,
                currentExp = 35000,
                requiredExp = 50000,
                totalExp = 485000,
                levelRewards = listOf(
                    LevelReward(5, 5000, "Badge", true),
                    LevelReward(10, 10000, "Frame (7d)", true),
                    LevelReward(15, 25000, "Mic Skin (7d)", true),
                    LevelReward(20, 50000, "Vehicle (7d)", false),
                    LevelReward(30, 100000, "Frame (30d)", false),
                    LevelReward(40, 200000, "Theme", false),
                    LevelReward(50, 500000, "Vehicle (30d)", false),
                    LevelReward(60, 1000000, "Premium Set", false),
                    LevelReward(80, 2500000, "Legendary Frame", false),
                    LevelReward(100, 10000000, "Legend Badge", false)
                )
            )
        }
    }
    
    fun claimReward(level: Int) {
        viewModelScope.launch {
            val updatedRewards = _uiState.value.levelRewards.map { reward ->
                if (reward.level == level && _uiState.value.currentLevel >= level) {
                    reward.copy(isClaimed = true)
                } else {
                    reward
                }
            }
            _uiState.value = _uiState.value.copy(levelRewards = updatedRewards)
        }
    }
}

data class LevelUiState(
    val isLoading: Boolean = false,
    val currentLevel: Int = 1,
    val currentExp: Long = 0,
    val requiredExp: Long = 1000,
    val totalExp: Long = 0,
    val levelRewards: List<LevelReward> = emptyList()
)
