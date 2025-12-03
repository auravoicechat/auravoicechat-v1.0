package com.aura.voicechat.ui.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Treasure Box ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class TreasureBoxViewModel @Inject constructor(
    // TODO: Inject repository when created
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TreasureBoxUiState())
    val uiState: StateFlow<TreasureBoxUiState> = _uiState.asStateFlow()
    
    init {
        loadBoxes()
    }
    
    private fun loadBoxes() {
        viewModelScope.launch {
            // TODO: Load from API
            val mockBoxes = listOf(
                TreasureBox(
                    id = "box_1",
                    name = "Silver Treasure",
                    description = "Open for small rewards",
                    level = 1,
                    minReward = 10,
                    maxReward = 100,
                    isAvailable = true,
                    unlockTime = ""
                ),
                TreasureBox(
                    id = "box_2",
                    name = "Gold Treasure",
                    description = "Open for medium rewards",
                    level = 2,
                    minReward = 100,
                    maxReward = 500,
                    isAvailable = true,
                    unlockTime = ""
                ),
                TreasureBox(
                    id = "box_3",
                    name = "Diamond Treasure",
                    description = "Open for big rewards",
                    level = 3,
                    minReward = 500,
                    maxReward = 2000,
                    isAvailable = false,
                    unlockTime = "2h 30m"
                )
            )
            
            _uiState.value = _uiState.value.copy(boxes = mockBoxes)
        }
    }
    
    fun openBox(boxId: String) {
        viewModelScope.launch {
            // TODO: Call API to open box
            val reward = TreasureReward(
                boxName = "Silver Treasure",
                amount = (10..100).random(),
                timestamp = "Just now"
            )
            
            _uiState.value = _uiState.value.copy(
                lastReward = reward,
                recentRewards = listOf(reward) + _uiState.value.recentRewards
            )
        }
    }
}

data class TreasureBoxUiState(
    val boxes: List<TreasureBox> = emptyList(),
    val recentRewards: List<TreasureReward> = emptyList(),
    val lastReward: TreasureReward? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
