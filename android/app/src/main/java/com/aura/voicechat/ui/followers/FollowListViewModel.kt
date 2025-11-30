package com.aura.voicechat.ui.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Follow List ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class FollowListViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(FollowListUiState())
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()
    
    fun loadList(type: String, userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Sample data
            val users = if (type == "followers") {
                listOf(
                    FollowUser("user1", "StarLight", null, 25, 3, true),
                    FollowUser("user2", "MoonRider", null, 18, 0, false),
                    FollowUser("user3", "DragonKing", null, 42, 5, true),
                    FollowUser("user4", "PhoenixFire", null, 15, 1, false),
                    FollowUser("user5", "IceQueen", null, 33, 2, true)
                )
            } else {
                listOf(
                    FollowUser("user1", "StarLight", null, 25, 3, true),
                    FollowUser("user3", "DragonKing", null, 42, 5, true),
                    FollowUser("user5", "IceQueen", null, 33, 2, true)
                )
            }
            
            _uiState.value = FollowListUiState(
                isLoading = false,
                users = users,
                totalCount = users.size
            )
        }
    }
    
    fun toggleFollow(userId: String) {
        viewModelScope.launch {
            val updatedUsers = _uiState.value.users.map { user ->
                if (user.userId == userId) {
                    user.copy(isFollowing = !user.isFollowing)
                } else {
                    user
                }
            }
            _uiState.value = _uiState.value.copy(users = updatedUsers)
        }
    }
}

data class FollowListUiState(
    val isLoading: Boolean = false,
    val users: List<FollowUser> = emptyList(),
    val totalCount: Int = 0
)
