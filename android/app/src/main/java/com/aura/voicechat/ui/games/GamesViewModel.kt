package com.aura.voicechat.ui.games

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Games ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class GamesViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()
    
    init {
        loadGames()
    }
    
    private fun loadGames() {
        viewModelScope.launch {
            _uiState.value = GamesUiState(
                coins = 2500000,
                currentJackpot = 125000000,
                games = listOf(
                    GameInfo(
                        id = "gift_wheel",
                        name = "Gift Wheel",
                        description = "Spin the wheel for amazing prizes!",
                        emoji = "🎡",
                        color = AccentMagenta,
                        minBet = 1000,
                        maxWinMultiplier = 100
                    ),
                    GameInfo(
                        id = "lucky_777",
                        name = "Lucky 777",
                        description = "Classic slot machine with huge jackpots",
                        emoji = "🎰",
                        color = VipGold,
                        minBet = 5000,
                        maxWinMultiplier = 1000
                    ),
                    GameInfo(
                        id = "lucky_77_pro",
                        name = "Lucky 77 Pro",
                        description = "Advanced slots with bonus rounds",
                        emoji = "💎",
                        color = DiamondBlue,
                        minBet = 10000,
                        maxWinMultiplier = 500
                    ),
                    GameInfo(
                        id = "lucky_fruit",
                        name = "Lucky Fruit",
                        description = "Match fruits for sweet rewards",
                        emoji = "🍒",
                        color = ErrorRed,
                        minBet = 2000,
                        maxWinMultiplier = 200
                    ),
                    GameInfo(
                        id = "greedy_baby",
                        name = "Greedy Baby",
                        description = "Feed the baby and win big",
                        emoji = "👶",
                        color = Purple80,
                        minBet = 1000,
                        maxWinMultiplier = 50
                    )
                ),
                recentWins = listOf(
                    RecentWin("1", "DragonKing", "Lucky 777", "🎰", 50000000, "2 min ago"),
                    RecentWin("2", "StarLight", "Gift Wheel", "🎡", 10000000, "5 min ago"),
                    RecentWin("3", "Phoenix99", "Lucky Fruit", "🍒", 5000000, "8 min ago"),
                    RecentWin("4", "MoonRider", "Lucky 77 Pro", "💎", 25000000, "15 min ago"),
                    RecentWin("5", "SunWarrior", "Greedy Baby", "👶", 2000000, "20 min ago")
                )
            )
        }
    }
}

data class GamesUiState(
    val isLoading: Boolean = false,
    val coins: Long = 0,
    val currentJackpot: Long = 0,
    val games: List<GameInfo> = emptyList(),
    val recentWins: List<RecentWin> = emptyList()
)
