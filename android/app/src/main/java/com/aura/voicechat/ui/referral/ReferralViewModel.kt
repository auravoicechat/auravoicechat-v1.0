package com.aura.voicechat.ui.referral

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Referral ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class ReferralViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReferralUiState())
    val uiState: StateFlow<ReferralUiState> = _uiState.asStateFlow()
    
    init {
        loadReferralData()
    }
    
    private fun loadReferralData() {
        viewModelScope.launch {
            _uiState.value = ReferralUiState(
                inviteCode = "AURA8X7K",
                totalCoinsEarned = 2500000,
                availableCoins = 450000,
                totalInvites = 25,
                minWithdrawCoins = 100000,
                hasBoundCode = false,
                totalCashEarned = 45.50,
                availableCash = 12.00,
                weeklyInvites = 8,
                minWithdrawCash = 10.00,
                recentInvites = listOf(
                    InviteRecord("1", "StarLight", "2 hours ago", 50000),
                    InviteRecord("2", "MoonRider", "Yesterday", 35000),
                    InviteRecord("3", "DragonKing", "2 days ago", 120000),
                    InviteRecord("4", "PhoenixFire", "3 days ago", 25000),
                    InviteRecord("5", "IceQueen", "1 week ago", 80000)
                )
            )
        }
    }
    
    fun shareInviteLink() {
        // Share intent
    }
    
    fun bindReferralCode(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(hasBoundCode = true)
        }
    }
    
    fun withdrawCoins() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(availableCoins = 0)
        }
    }
    
    fun withdrawCash() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(availableCash = 0.0)
        }
    }
}

data class ReferralUiState(
    val isLoading: Boolean = false,
    val inviteCode: String = "",
    val totalCoinsEarned: Long = 0,
    val availableCoins: Long = 0,
    val totalInvites: Int = 0,
    val minWithdrawCoins: Long = 100000,
    val hasBoundCode: Boolean = false,
    val totalCashEarned: Double = 0.0,
    val availableCash: Double = 0.0,
    val weeklyInvites: Int = 0,
    val minWithdrawCash: Double = 10.0,
    val recentInvites: List<InviteRecord> = emptyList()
)
