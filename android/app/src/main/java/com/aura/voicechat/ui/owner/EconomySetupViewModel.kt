package com.aura.voicechat.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Economy Setup
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class EconomySetupViewModel @Inject constructor(
    // private val repository: EconomyRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(EconomySetupState())
    val state: StateFlow<EconomySetupState> = _state.asStateFlow()
    
    init {
        loadEconomyConfig()
    }
    
    private fun loadEconomyConfig() {
        viewModelScope.launch {
            // TODO: Load from repository
            // Sample data with current app economy
            _state.value = _state.value.copy(
                userTargets = listOf(
                    TargetConfig(1, "Bronze Target", 30_000, 10.0, 3_000),
                    TargetConfig(2, "Silver Target", 70_000, 20.0, 7_000),
                    TargetConfig(3, "Gold Target", 150_000, 45.0, 15_000),
                    TargetConfig(4, "Platinum Target", 300_000, 90.0, 30_000),
                    TargetConfig(5, "Diamond Target", 700_000, 210.0, 70_000)
                ),
                guideTargets = listOf(
                    TargetConfig(1, "Guide Bronze", 50_000, 15.0, 5_000),
                    TargetConfig(2, "Guide Silver", 100_000, 30.0, 10_000),
                    TargetConfig(3, "Guide Gold", 200_000, 60.0, 20_000),
                    TargetConfig(4, "Guide Platinum", 500_000, 150.0, 50_000),
                    TargetConfig(5, "Guide Diamond", 1_000_000, 300.0, 100_000)
                ),
                diamondToCashRate = 3333.33, // 100,000 💎 = $30
                diamondToCoinRate = 30, // 30% conversion
                coinValue = 0.001, // 1000 coins = $1
                minCashout = 10.0,
                maxCashout = 10000.0,
                clearanceDays = 5,
                maxGiftQuantity = 999,
                dailyDiamondLimit = 100_000
            )
        }
    }
    
    fun updateTarget(target: TargetConfig) {
        viewModelScope.launch {
            val updatedTargets = _state.value.userTargets.map {
                if (it.tier == target.tier) target else it
            }
            _state.value = _state.value.copy(userTargets = updatedTargets)
        }
    }
    
    fun addNewTarget() {
        viewModelScope.launch {
            val newTier = _state.value.userTargets.size + 1
            val newTarget = TargetConfig(
                tier = newTier,
                name = "New Tier $newTier",
                requiredDiamonds = 0,
                cashReward = 0.0,
                bonusCoins = 0
            )
            _state.value = _state.value.copy(
                userTargets = _state.value.userTargets + newTarget
            )
        }
    }
    
    fun saveTargets() {
        viewModelScope.launch {
            // TODO: Save to repository
            // repository.saveEconomyConfig(_state.value)
        }
    }
    
    fun updateDiamondToCashRate(rate: String) {
        val newRate = rate.toDoubleOrNull() ?: return
        _state.value = _state.value.copy(diamondToCashRate = newRate)
        saveConfig()
    }
    
    fun updateDiamondToCoinRate(rate: String) {
        val newRate = rate.replace("%", "").toIntOrNull() ?: return
        _state.value = _state.value.copy(diamondToCoinRate = newRate)
        saveConfig()
    }
    
    fun updateCoinValue(value: String) {
        val newValue = value.toDoubleOrNull() ?: return
        _state.value = _state.value.copy(coinValue = newValue)
        saveConfig()
    }
    
    fun updateMinCashout(value: String) {
        val newValue = value.toDoubleOrNull() ?: return
        _state.value = _state.value.copy(minCashout = newValue)
        saveConfig()
    }
    
    fun updateMaxCashout(value: String) {
        val newValue = value.toDoubleOrNull() ?: return
        _state.value = _state.value.copy(maxCashout = newValue)
        saveConfig()
    }
    
    fun updateClearanceDays(value: String) {
        val newValue = value.toIntOrNull() ?: return
        _state.value = _state.value.copy(clearanceDays = newValue)
        saveConfig()
    }
    
    fun updateMaxGiftQuantity(value: String) {
        val newValue = value.toIntOrNull() ?: return
        _state.value = _state.value.copy(maxGiftQuantity = newValue)
        saveConfig()
    }
    
    fun updateDailyDiamondLimit(value: String) {
        val newValue = value.toLongOrNull() ?: return
        _state.value = _state.value.copy(dailyDiamondLimit = newValue)
        saveConfig()
    }
    
    private fun saveConfig() {
        viewModelScope.launch {
            // TODO: Auto-save after each change
            // repository.saveEconomyConfig(_state.value)
        }
    }
}

data class EconomySetupState(
    val userTargets: List<TargetConfig> = emptyList(),
    val guideTargets: List<TargetConfig> = emptyList(),
    val diamondToCashRate: Double = 3333.33,
    val diamondToCoinRate: Int = 30,
    val coinValue: Double = 0.001,
    val minCashout: Double = 10.0,
    val maxCashout: Double = 10000.0,
    val clearanceDays: Int = 5,
    val maxGiftQuantity: Int = 999,
    val dailyDiamondLimit: Long = 100_000
)
