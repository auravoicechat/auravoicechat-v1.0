package com.aura.voicechat.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.RankingUser
import com.aura.voicechat.data.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Leaderboard ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()
    
    private val _rankings = MutableStateFlow<List<RankingUser>>(emptyList())
    val rankings: StateFlow<List<RankingUser>> = _rankings.asStateFlow()
    
    private val _myRank = MutableStateFlow<RankingUser?>(null)
    val myRank: StateFlow<RankingUser?> = _myRank.asStateFlow()
    
    private val _selectedType = MutableStateFlow("wealth")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()
    
    private val _selectedPeriod = MutableStateFlow("daily")
    val selectedPeriod: StateFlow<String> = _selectedPeriod.asStateFlow()
    
    init {
        loadLeaderboard("wealth", "daily")
    }
    
    fun loadLeaderboard(type: String, period: String) {
        viewModelScope.launch {
            _selectedType.value = type
            _selectedPeriod.value = period
            _uiState.value = LeaderboardUiState.Loading
            
            // Load leaderboard
            leaderboardRepository.getLeaderboard(type, period).collect { result ->
                result.onSuccess { users ->
                    _rankings.value = users
                    _uiState.value = LeaderboardUiState.Success
                }.onFailure { error ->
                    _uiState.value = LeaderboardUiState.Error(error.message ?: "Failed to load leaderboard")
                }
            }
            
            // Load my rank
            leaderboardRepository.getMyRank(type).collect { result ->
                result.onSuccess { rank ->
                    _myRank.value = rank
                }.onFailure { /* Handle silently */ }
            }
        }
    }
}

sealed class LeaderboardUiState {
    object Loading : LeaderboardUiState()
    object Success : LeaderboardUiState()
    data class Error(val message: String) : LeaderboardUiState()
}
