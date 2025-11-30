package com.aura.voicechat.ui.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * ViewModel for Lucky 77 Pro Game
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class Lucky77ProViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(Lucky77ProUiState())
    val uiState: StateFlow<Lucky77ProUiState> = _uiState.asStateFlow()

    private val proSymbols = listOf("💎", "👑", "🔥", "⭐", "💰", "🎰", "77")

    init {
        loadGameData()
    }

    private fun loadGameData() {
        _uiState.update { state ->
            state.copy(
                userDiamonds = 50000, // From user repository
                jackpotAmount = 10000000,
                slots = listOf("💎", "💎", "💎"),
                recentWins = listOf(
                    ProWinRecord("VIP★Player", 25000, System.currentTimeMillis()),
                    ProWinRecord("DiamondKing", 15000, System.currentTimeMillis() - 60000),
                    ProWinRecord("ProGamer77", 50000, System.currentTimeMillis() - 120000)
                )
            )
        }
    }

    fun selectMultiplier(multiplier: Int) {
        _uiState.update { state ->
            state.copy(selectedMultiplier = multiplier)
        }
    }

    fun selectBet(bet: Int) {
        _uiState.update { state ->
            state.copy(selectedBet = bet)
        }
    }

    fun spin() {
        val currentState = _uiState.value
        val cost = currentState.totalCost

        if (currentState.userDiamonds < cost) {
            return
        }

        viewModelScope.launch {
            // Start spinning
            _uiState.update { state ->
                state.copy(
                    isSpinning = true,
                    userDiamonds = state.userDiamonds - cost
                )
            }

            // Animate slots
            repeat(20) {
                _uiState.update { state ->
                    state.copy(
                        slots = List(3) { proSymbols.random() }
                    )
                }
                delay(100)
            }

            // Determine final result
            val result = determineProResult()
            
            _uiState.update { state ->
                state.copy(
                    slots = result.symbols,
                    isSpinning = false
                )
            }

            delay(300)

            // Calculate winnings
            val winAmount = calculateProWin(result.symbols, currentState.selectedBet, currentState.selectedMultiplier)
            val isJackpot = result.symbols.all { it == "77" }

            if (winAmount > 0) {
                _uiState.update { state ->
                    state.copy(
                        lastWinAmount = winAmount,
                        isJackpot = isJackpot,
                        showWinDialog = true,
                        userDiamonds = state.userDiamonds + winAmount
                    )
                }
            }
        }
    }

    private fun determineProResult(): SpinResult {
        val random = Random.nextFloat()
        
        return when {
            random < 0.001f -> { // Jackpot 0.1%
                SpinResult(listOf("77", "77", "77"))
            }
            random < 0.01f -> { // Triple match 1%
                val symbol = proSymbols.filter { it != "77" }.random()
                SpinResult(listOf(symbol, symbol, symbol))
            }
            random < 0.1f -> { // Double match 10%
                val symbol = proSymbols.random()
                val other = proSymbols.filter { it != symbol }.random()
                SpinResult(listOf(symbol, symbol, other).shuffled())
            }
            else -> { // Random
                SpinResult(List(3) { proSymbols.random() })
            }
        }
    }

    private fun calculateProWin(symbols: List<String>, bet: Int, multiplier: Int): Int {
        val baseBet = bet * multiplier
        
        return when {
            symbols.all { it == "77" } -> baseBet * 777 // Jackpot
            symbols.all { it == "💎" } -> baseBet * 100
            symbols.all { it == "👑" } -> baseBet * 77
            symbols.all { it == "🔥" } -> baseBet * 50
            symbols.all { it == "⭐" } -> baseBet * 40
            symbols.all { it == "💰" } -> baseBet * 30
            symbols.all { it == "🎰" } -> baseBet * 25
            symbols.distinct().size == 2 -> baseBet * 3 // Two of a kind
            else -> 0
        }
    }

    fun dismissWinDialog() {
        _uiState.update { state ->
            state.copy(
                showWinDialog = false,
                isJackpot = false
            )
        }
    }
}

data class Lucky77ProUiState(
    val isLoading: Boolean = false,
    val userDiamonds: Int = 0,
    val jackpotAmount: Long = 0,
    val slots: List<String> = listOf("💎", "💎", "💎"),
    val isSpinning: Boolean = false,
    val selectedBet: Int = 100,
    val selectedMultiplier: Int = 1,
    val lastWinAmount: Int = 0,
    val isJackpot: Boolean = false,
    val showWinDialog: Boolean = false,
    val recentWins: List<ProWinRecord> = emptyList(),
    val error: String? = null
) {
    val totalCost: Int
        get() = selectedBet * selectedMultiplier

    val canSpin: Boolean
        get() = userDiamonds >= totalCost && !isSpinning
}

private data class SpinResult(
    val symbols: List<String>
)
