package com.aura.voicechat.ui.cp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * CP System ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class CpViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(CpUiState())
    val uiState: StateFlow<CpUiState> = _uiState.asStateFlow()
    
    init {
        loadCpData()
    }
    
    private fun loadCpData() {
        viewModelScope.launch {
            // Sample data - would come from API
            _uiState.value = CpUiState(
                hasPartner = true,
                partnerId = "partner123",
                partnerName = "StarLight",
                partnerAvatar = null,
                cpLevel = 5,
                cpExp = 45000,
                cpExpRequired = 100000,
                cpDays = 45,
                currentBenefits = listOf(
                    "Exclusive CP Frame",
                    "CP Chat Bubble",
                    "CP Entry Animation",
                    "2x Gift EXP when gifting partner",
                    "Special CP Badge"
                ),
                dailyTasks = listOf(
                    CpTask("task1", "Send gift to CP", 3, 5, 500, false, false),
                    CpTask("task2", "Chat with CP", 10, 10, 200, true, false),
                    CpTask("task3", "Be in same room for 30min", 20, 30, 300, false, false),
                    CpTask("task4", "Like CP's message", 5, 5, 100, true, true)
                ),
                cpCosmetics = listOf(
                    CpCosmetic("frame1", "Love Frame Lv.1", "frame", 1),
                    CpCosmetic("frame2", "Love Frame Lv.3", "frame", 3),
                    CpCosmetic("vehicle1", "Love Carriage", "vehicle", 5),
                    CpCosmetic("bubble1", "Heart Bubble", "bubble", 2),
                    CpCosmetic("theme1", "Romance Theme", "theme", 7),
                    CpCosmetic("frame3", "Eternal Love Frame", "frame", 10)
                )
            )
        }
    }
    
    fun showFindPartnerDialog() {
        _uiState.value = _uiState.value.copy(showFindPartnerDialog = true)
    }
    
    fun dismissFindPartnerDialog() {
        _uiState.value = _uiState.value.copy(showFindPartnerDialog = false)
    }
    
    fun sendCpRequest(userId: String) {
        viewModelScope.launch {
            // Call API to send CP request
            dismissFindPartnerDialog()
        }
    }
    
    fun claimTaskReward(taskId: String) {
        viewModelScope.launch {
            // Call API to claim reward
            val tasks = _uiState.value.dailyTasks.map { task ->
                if (task.id == taskId) task.copy(isClaimed = true) else task
            }
            _uiState.value = _uiState.value.copy(dailyTasks = tasks)
        }
    }
}

data class CpUiState(
    val isLoading: Boolean = false,
    val hasPartner: Boolean = false,
    val partnerId: String = "",
    val partnerName: String = "",
    val partnerAvatar: String? = null,
    val cpLevel: Int = 0,
    val cpExp: Long = 0,
    val cpExpRequired: Long = 0,
    val cpDays: Int = 0,
    val currentBenefits: List<String> = emptyList(),
    val dailyTasks: List<CpTask> = emptyList(),
    val cpCosmetics: List<CpCosmetic> = emptyList(),
    val showFindPartnerDialog: Boolean = false
)
