package com.aura.voicechat.ui.vip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VIP System ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class VipViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(VipUiState())
    val uiState: StateFlow<VipUiState> = _uiState.asStateFlow()
    
    init {
        loadVipData()
    }
    
    private fun loadVipData() {
        viewModelScope.launch {
            _uiState.value = VipUiState(
                currentTier = 3,
                daysRemaining = 25,
                totalDiamondsSpent = 2500000,
                nextTierProgress = 0.65f,
                allTiers = (1..10).toList(),
                purchasePackages = listOf(
                    VipPackage("pkg_week", "Weekly VIP", "7 days of VIP benefits", 4.99, 6.99, 50000, 7),
                    VipPackage("pkg_month", "Monthly VIP", "30 days of VIP benefits", 14.99, 24.99, 250000, 30, isBestValue = true),
                    VipPackage("pkg_season", "Seasonal VIP", "90 days of VIP benefits", 39.99, 59.99, 800000, 90)
                )
            )
        }
    }
    
    fun getBenefitsForTier(tier: Int): List<VipBenefit> {
        val baseBenefits = mutableListOf(
            VipBenefit("Daily Bonus", "${100 + tier * 20}% daily reward multiplier"),
            VipBenefit("VIP Badge", "Exclusive V$tier badge on profile"),
            VipBenefit("Priority Support", "24/7 dedicated support")
        )
        
        if (tier >= 2) baseBenefits.add(VipBenefit("VIP Frame", "Exclusive frame for V$tier"))
        if (tier >= 3) baseBenefits.add(VipBenefit("Gift Discount", "${tier * 2}% discount on gifts"))
        if (tier >= 4) baseBenefits.add(VipBenefit("VIP Vehicle", "Exclusive entrance vehicle"))
        if (tier >= 5) baseBenefits.add(VipBenefit("Room Priority", "Featured in room listings"))
        if (tier >= 6) baseBenefits.add(VipBenefit("Custom Theme", "Exclusive VIP theme"))
        if (tier >= 7) baseBenefits.add(VipBenefit("VIP Emojis", "Exclusive animated emojis"))
        if (tier >= 8) baseBenefits.add(VipBenefit("Withdrawal Bonus", "10% extra on withdrawals"))
        if (tier >= 9) baseBenefits.add(VipBenefit("VIP Events", "Access to exclusive events"))
        if (tier >= 10) baseBenefits.add(VipBenefit("Legend Status", "Permanent legend recognition"))
        
        return baseBenefits
    }
    
    fun getExclusiveItems(tier: Int): List<VipExclusiveItemData> {
        return listOf(
            VipExclusiveItemData("frame_v$tier", "V$tier Frame", "frame"),
            VipExclusiveItemData("badge_v$tier", "V$tier Badge", "badge"),
            VipExclusiveItemData("vehicle_v$tier", "V$tier Vehicle", "vehicle"),
            VipExclusiveItemData("theme_v$tier", "V$tier Theme", "theme")
        )
    }
    
    fun purchaseVip(pkg: VipPackage) {
        viewModelScope.launch {
            // In production, this would initiate payment flow
        }
    }
}

data class VipUiState(
    val isLoading: Boolean = false,
    val currentTier: Int = 0,
    val daysRemaining: Int = 0,
    val totalDiamondsSpent: Long = 0,
    val nextTierProgress: Float = 0f,
    val allTiers: List<Int> = emptyList(),
    val purchasePackages: List<VipPackage> = emptyList()
)
