package com.aura.voicechat.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ranking ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class RankingViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()
    
    fun loadRanking(type: String, period: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Sample data
            val rankings = when (type) {
                "sender" -> listOf(
                    RankingItemData("user1", "DragonKing", null, 60, 125000000),
                    RankingItemData("user2", "PhoenixQueen", null, 55, 98000000),
                    RankingItemData("user3", "StarLord", null, 48, 75000000),
                    RankingItemData("user4", "MoonWalker", null, 45, 62000000),
                    RankingItemData("user5", "SunWarrior", null, 42, 55000000),
                    RankingItemData("user6", "IceKnight", null, 40, 48000000),
                    RankingItemData("user7", "FireMage", null, 38, 42000000),
                    RankingItemData("user8", "ThunderBolt", null, 35, 38000000),
                    RankingItemData("user9", "ShadowNinja", null, 32, 32000000),
                    RankingItemData("user10", "LightAngel", null, 30, 28000000)
                )
                "receiver" -> listOf(
                    RankingItemData("user11", "DiamondsQueen", null, 58, 200000000),
                    RankingItemData("user12", "GoldPrincess", null, 52, 165000000),
                    RankingItemData("user13", "SilverStar", null, 47, 142000000),
                    RankingItemData("user14", "RubyHeart", null, 44, 125000000),
                    RankingItemData("user15", "EmeraldEyes", null, 41, 108000000),
                    RankingItemData("user16", "SapphireDream", null, 38, 95000000),
                    RankingItemData("user17", "PearlGlow", null, 35, 82000000),
                    RankingItemData("user18", "OpalShine", null, 33, 70000000),
                    RankingItemData("user19", "AmethystMist", null, 30, 58000000),
                    RankingItemData("user20", "TopazFlare", null, 28, 48000000)
                )
                "family" -> listOf(
                    RankingItemData("fam1", "Dragon Warriors", null, 0, 850000000),
                    RankingItemData("fam2", "Phoenix Rising", null, 0, 720000000),
                    RankingItemData("fam3", "Star Alliance", null, 0, 650000000),
                    RankingItemData("fam4", "Moon Clan", null, 0, 580000000),
                    RankingItemData("fam5", "Sun Dynasty", null, 0, 520000000),
                    RankingItemData("fam6", "Ice Kingdom", null, 0, 465000000),
                    RankingItemData("fam7", "Fire Legion", null, 0, 410000000),
                    RankingItemData("fam8", "Thunder Guild", null, 0, 365000000),
                    RankingItemData("fam9", "Shadow Order", null, 0, 320000000),
                    RankingItemData("fam10", "Light Brigade", null, 0, 280000000)
                )
                else -> emptyList()
            }
            
            _uiState.value = RankingUiState(
                isLoading = false,
                rankings = rankings,
                myRank = 156,
                myRankingItem = RankingItemData("me", "MyUsername", null, 15, 250000)
            )
        }
    }
}

data class RankingUiState(
    val isLoading: Boolean = false,
    val rankings: List<RankingItemData> = emptyList(),
    val myRank: Int = 0,
    val myRankingItem: RankingItemData? = null
)
