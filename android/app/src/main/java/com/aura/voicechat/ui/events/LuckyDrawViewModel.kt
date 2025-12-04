package com.aura.voicechat.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Lucky Draw Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class LuckyDrawViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(LuckyDrawState())
    val state: StateFlow<LuckyDrawState> = _state.asStateFlow()
    
    init {
        loadPrizes()
    }
    
    private fun loadPrizes() {
        // Sample prizes for the wheel
        val prizes = listOf(
            Prize("1", "100 Coins", "url", 100, "COINS"),
            Prize("2", "500 Coins", "url", 500, "COINS"),
            Prize("3", "1000 Coins", "url", 1000, "COINS"),
            Prize("4", "50 Diamonds", "url", 50, "DIAMONDS"),
            Prize("5", "100 Diamonds", "url", 100, "DIAMONDS"),
            Prize("6", "VIP 1 Day", "url", 1, "VIP")
        )
        _state.value = _state.value.copy(prizes = prizes)
    }
    
    fun loadDrawHistory() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            repository.getLuckyDrawHistory().fold(
                onSuccess = { response ->
                    val history = response.draws.map { dto ->
                        DrawHistory(
                            id = dto.id,
                            prize = Prize(
                                id = dto.prize.id,
                                name = dto.prize.name,
                                iconUrl = dto.prize.iconUrl,
                                value = dto.prize.value,
                                type = dto.prize.type
                            ),
                            timestamp = dto.timestamp
                        )
                    }
                    
                    _state.value = _state.value.copy(
                        history = history,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
    
    fun spin() {
        if (_state.value.remainingTickets <= 0 || _state.value.isSpinning) {
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isSpinning = true, wonPrize = null)
            
            // Simulate spinning animation
            delay(3000)
            
            repository.luckyDraw(1).fold(
                onSuccess = { result ->
                    val wonPrize = result.prizes.firstOrNull()?.let { dto ->
                        Prize(
                            id = dto.id,
                            name = dto.name,
                            iconUrl = dto.iconUrl,
                            value = dto.value,
                            type = dto.type
                        )
                    }
                    
                    _state.value = _state.value.copy(
                        isSpinning = false,
                        wonPrize = wonPrize,
                        remainingTickets = result.remainingTickets
                    )
                    
                    // Reload history
                    loadDrawHistory()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isSpinning = false,
                        error = error.message
                    )
                }
            )
        }
    }
}

data class LuckyDrawState(
    val prizes: List<Prize> = emptyList(),
    val history: List<DrawHistory> = emptyList(),
    val remainingTickets: Int = 0,
    val isSpinning: Boolean = false,
    val wonPrize: Prize? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
