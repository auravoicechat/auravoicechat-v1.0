package com.aura.voicechat.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Friends System ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class FriendsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()
    
    private var allFriends: List<Friend> = emptyList()
    
    init {
        loadFriends()
    }
    
    private fun loadFriends() {
        viewModelScope.launch {
            allFriends = listOf(
                Friend("user1", "StarLight", null, true, "", 8),
                Friend("user2", "MoonRider", null, true, "", 5),
                Friend("user3", "SunWarrior", null, false, "2 hours ago", 3),
                Friend("user4", "DragonKing", null, false, "5 hours ago", 10),
                Friend("user5", "PhoenixFire", null, false, "1 day ago", 2),
                Friend("user6", "IceQueen", null, true, "", 6),
                Friend("user7", "ThunderBolt", null, false, "3 days ago", 1)
            )
            
            _uiState.value = FriendsUiState(
                friends = allFriends,
                filteredFriends = allFriends,
                requests = listOf(
                    FriendRequest("req1", "NewUser123", null, "2 hours ago"),
                    FriendRequest("req2", "CoolGuy99", null, "1 day ago")
                ),
                sentRequests = listOf(
                    FriendRequest("sent1", "AwesomePerson", null, "5 hours ago")
                )
            )
        }
    }
    
    fun search(query: String) {
        val filtered = if (query.isEmpty()) {
            allFriends
        } else {
            allFriends.filter { it.name.contains(query, ignoreCase = true) }
        }
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredFriends = filtered
        )
    }
    
    fun sendFriendRequest(userId: String) {
        viewModelScope.launch {
            // Call API to send friend request
        }
    }
    
    fun acceptRequest(userId: String) {
        viewModelScope.launch {
            val requests = _uiState.value.requests.filter { it.userId != userId }
            _uiState.value = _uiState.value.copy(requests = requests)
        }
    }
    
    fun rejectRequest(userId: String) {
        viewModelScope.launch {
            val requests = _uiState.value.requests.filter { it.userId != userId }
            _uiState.value = _uiState.value.copy(requests = requests)
        }
    }
    
    fun cancelRequest(userId: String) {
        viewModelScope.launch {
            val sentRequests = _uiState.value.sentRequests.filter { it.userId != userId }
            _uiState.value = _uiState.value.copy(sentRequests = sentRequests)
        }
    }
    
    fun removeFriend(userId: String) {
        viewModelScope.launch {
            allFriends = allFriends.filter { it.userId != userId }
            _uiState.value = _uiState.value.copy(
                friends = allFriends,
                filteredFriends = allFriends
            )
        }
    }
}

data class FriendsUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val friends: List<Friend> = emptyList(),
    val filteredFriends: List<Friend> = emptyList(),
    val requests: List<FriendRequest> = emptyList(),
    val sentRequests: List<FriendRequest> = emptyList()
)
