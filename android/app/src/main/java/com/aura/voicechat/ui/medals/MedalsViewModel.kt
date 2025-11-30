package com.aura.voicechat.ui.medals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Medals ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class MedalsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(MedalsUiState())
    val uiState: StateFlow<MedalsUiState> = _uiState.asStateFlow()
    
    init {
        loadMedals()
    }
    
    private fun loadMedals() {
        viewModelScope.launch {
            val medals = listOf(
                // Gift Medals
                Medal("gift_sender_1", "Gift Sender I", "Send 10K coins in gifts", "gift", 10000, 10000, true),
                Medal("gift_sender_2", "Gift Sender II", "Send 100K coins in gifts", "gift", 75000, 100000),
                Medal("gift_sender_3", "Gift Sender III", "Send 1M coins in gifts", "gift", 250000, 1000000),
                Medal("gift_receiver_1", "Gift Receiver I", "Receive 10K diamonds", "gift", 10000, 10000, true),
                Medal("gift_receiver_2", "Gift Receiver II", "Receive 100K diamonds", "gift", 45000, 100000),
                
                // Achievement Medals
                Medal("level_10", "Rising Star", "Reach Level 10", "achievement", 10, 10, true),
                Medal("level_20", "Established", "Reach Level 20", "achievement", 15, 20),
                Medal("level_40", "Expert", "Reach Level 40", "achievement", 15, 40),
                Medal("level_60", "Master", "Reach Level 60", "achievement", 15, 60),
                
                // Activity Medals
                Medal("login_7d", "7 Day Streak", "Login for 7 days", "activity", 7, 7, true),
                Medal("login_30d", "30 Day Veteran", "Login for 30 days", "activity", 30, 30, true),
                Medal("login_60d", "60 Day Regular", "Login for 60 days", "activity", 45, 60),
                Medal("login_90d", "90 Day Dedicated", "Login for 90 days", "activity", 45, 90),
                Medal("login_180d", "180 Day Loyal", "Login for 180 days", "activity", 45, 180),
                Medal("login_365d", "365 Day Legend", "Login for 365 days", "activity", 45, 365),
                
                // Special Medals
                Medal("cp_first", "First Love", "Form your first CP", "special", 1, 1, true),
                Medal("cp_level_5", "Growing Love", "Reach CP Level 5", "special", 3, 5),
                Medal("cp_level_10", "Eternal Love", "Reach CP Level 10", "special", 3, 10),
                Medal("family_member", "Family Member", "Join a family", "special", 1, 1, true),
                Medal("family_founder", "Family Founder", "Create a family", "special", 0, 1)
            )
            
            val earned = medals.count { it.progress >= it.target }
            val displayed = medals.filter { it.isDisplayed }.map { it.id }
            
            _uiState.value = MedalsUiState(
                medals = medals,
                totalMedals = medals.size,
                earnedMedals = earned,
                displayedMedals = displayed
            )
        }
    }
    
    fun toggleMedalDisplay(medalId: String) {
        viewModelScope.launch {
            val currentDisplayed = _uiState.value.displayedMedals.toMutableList()
            val medals = _uiState.value.medals.map { medal ->
                if (medal.id == medalId && medal.progress >= medal.target) {
                    val newDisplayed = !medal.isDisplayed
                    if (newDisplayed && currentDisplayed.size >= 10) {
                        // Max 10 displayed
                        medal
                    } else {
                        if (newDisplayed) {
                            currentDisplayed.add(medalId)
                        } else {
                            currentDisplayed.remove(medalId)
                        }
                        medal.copy(isDisplayed = newDisplayed)
                    }
                } else {
                    medal
                }
            }
            
            _uiState.value = _uiState.value.copy(
                medals = medals,
                displayedMedals = currentDisplayed
            )
        }
    }
}

data class MedalsUiState(
    val isLoading: Boolean = false,
    val medals: List<Medal> = emptyList(),
    val totalMedals: Int = 0,
    val earnedMedals: Int = 0,
    val displayedMedals: List<String> = emptyList()
)
