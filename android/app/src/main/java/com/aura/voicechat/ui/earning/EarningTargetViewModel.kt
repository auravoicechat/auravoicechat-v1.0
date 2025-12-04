package com.aura.voicechat.ui.earning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Earning Target Sheet Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class EarningTargetViewModel @Inject constructor(
    // private val repository: EarningRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(EarningTargetState())
    val state: StateFlow<EarningTargetState> = _state.asStateFlow()
    
    fun loadTargets(isGuide: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // TODO: Load from repository
            // val targets = repository.getEarningTargets(isGuide)
            
            // Sample data
            val sampleTargets = if (isGuide) {
                listOf(
                    TargetItem(1, "Guide Bronze", 50_000, 15.0, 5_000, "Monthly"),
                    TargetItem(2, "Guide Silver", 100_000, 30.0, 10_000, "Monthly"),
                    TargetItem(3, "Guide Gold", 200_000, 60.0, 20_000, "Monthly"),
                    TargetItem(4, "Guide Platinum", 500_000, 150.0, 50_000, "Monthly"),
                    TargetItem(5, "Guide Diamond", 1_000_000, 300.0, 100_000, "Monthly")
                )
            } else {
                listOf(
                    TargetItem(1, "Bronze Target", 30_000, 10.0, 3_000, "Monthly"),
                    TargetItem(2, "Silver Target", 70_000, 20.0, 7_000, "Monthly"),
                    TargetItem(3, "Gold Target", 150_000, 45.0, 15_000, "Monthly"),
                    TargetItem(4, "Platinum Target", 300_000, 90.0, 30_000, "Monthly"),
                    TargetItem(5, "Diamond Target", 700_000, 210.0, 70_000, "Monthly")
                )
            }
            
            // Sample progress (if user has active target)
            val sampleProgress = EarningProgress(
                currentDiamonds = 45_000,
                targetDiamonds = 100_000,
                progressPercentage = 45
            )
            
            _state.value = _state.value.copy(
                targets = sampleTargets,
                currentProgress = sampleProgress,
                isLoading = false
            )
        }
    }
}

data class EarningTargetState(
    val targets: List<TargetItem> = emptyList(),
    val currentProgress: EarningProgress? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
