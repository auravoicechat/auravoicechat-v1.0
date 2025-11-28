package com.aura.voicechat.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.model.RoomCard
import com.aura.voicechat.domain.model.RoomType
import com.aura.voicechat.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Home ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadRooms()
        loadBanners()
    }
    
    private fun loadRooms() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Load mock data for now
                val popularRooms = listOf(
                    RoomCard(
                        id = "room_1",
                        name = "Music Lounge 🎵",
                        coverImage = null,
                        ownerName = "DJ Mike",
                        ownerAvatar = null,
                        type = RoomType.MUSIC,
                        userCount = 45,
                        capacity = 100,
                        isLive = true,
                        tags = listOf("Music", "Chill", "English")
                    ),
                    RoomCard(
                        id = "room_2",
                        name = "Late Night Talk",
                        coverImage = null,
                        ownerName = "Sarah",
                        ownerAvatar = null,
                        type = RoomType.VOICE,
                        userCount = 23,
                        capacity = 50,
                        isLive = true,
                        tags = listOf("Talk", "Dating")
                    ),
                    RoomCard(
                        id = "room_3",
                        name = "Gaming Zone 🎮",
                        coverImage = null,
                        ownerName = "GamerPro",
                        ownerAvatar = null,
                        type = RoomType.VIDEO,
                        userCount = 67,
                        capacity = 100,
                        isLive = true,
                        tags = listOf("Gaming", "Fun")
                    )
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    popularRooms = popularRooms,
                    myRooms = emptyList()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    private fun loadBanners() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                banners = listOf("banner1", "banner2", "banner3")
            )
        }
    }
    
    fun refresh() {
        loadRooms()
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val myRooms: List<RoomCard> = emptyList(),
    val popularRooms: List<RoomCard> = emptyList(),
    val banners: List<String> = emptyList(),
    val error: String? = null
)
