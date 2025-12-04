package com.aura.voicechat.ui.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Guide Panel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class GuidePanelViewModel @Inject constructor(
    // private val guideRepository: GuideRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(GuidePanelState())
    val state: StateFlow<GuidePanelState> = _state.asStateFlow()
    
    init {
        loadGuideProfile()
    }
    
    private fun loadGuideProfile() {
        viewModelScope.launch {
            // TODO: Load from repository
            // val profile = guideRepository.getMyGuideProfile()
            
            _state.value = _state.value.copy(
                level = 5,
                rating = 4.8f,
                isActive = true,
                monthlySessions = 127,
                monthlyEarnings = 285.50,
                monthlyDiamonds = 950_000,
                targetProgress = 75,
                currentTargetDiamonds = 750_000,
                targetDiamonds = 1_000_000
            )
        }
    }
}

data class GuidePanelState(
    val level: Int = 1,
    val rating: Float = 0f,
    val isActive: Boolean = false,
    val monthlySessions: Int = 0,
    val monthlyEarnings: Double = 0.0,
    val monthlyDiamonds: Long = 0,
    val targetProgress: Int = 0,
    val currentTargetDiamonds: Long = 0,
    val targetDiamonds: Long = 0
)
