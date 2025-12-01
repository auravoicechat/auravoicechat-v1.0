package com.aura.voicechat.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.model.RoomCard
import com.aura.voicechat.domain.model.RoomType
import com.aura.voicechat.domain.repository.AuthRepository
import com.aura.voicechat.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Home ViewModel (Live API Connected - No Mock Data)
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Fetches rooms, banners, and user data from the backend API.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val authRepository: AuthRepository,
    private val apiService: ApiService
) : ViewModel() {
    
    companion object {
        private const val TAG = "HomeViewModel"
    }
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadUserInfo()
        loadAllRooms()
        loadBanners()
    }
    
    /**
     * Load current user information from backend.
     */
    private fun loadUserInfo() {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId()
                _uiState.value = _uiState.value.copy(currentUserId = userId)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user info", e)
            }
        }
    }
    
    /**
     * Load all room categories from the backend API.
     */
    private fun loadAllRooms() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Load popular rooms from backend
                val popularResult = roomRepository.getPopularRooms()
                popularResult.fold(
                    onSuccess = { rooms ->
                        Log.d(TAG, "Loaded ${rooms.size} popular rooms")
                        _uiState.value = _uiState.value.copy(popularRooms = rooms)
                    },
                    onFailure = { e ->
                        Log.e(TAG, "Failed to load popular rooms", e)
                        _uiState.value = _uiState.value.copy(error = "Failed to load rooms: ${e.message}")
                    }
                )
                
                // Load user's own rooms from backend
                val myRoomsResult = roomRepository.getMyRooms()
                myRoomsResult.fold(
                    onSuccess = { rooms ->
                        Log.d(TAG, "Loaded ${rooms.size} my rooms")
                        _uiState.value = _uiState.value.copy(
                            myRooms = rooms,
                            isNewUser = rooms.isEmpty()
                        )
                    },
                    onFailure = { e ->
                        Log.e(TAG, "Failed to load my rooms", e)
                        _uiState.value = _uiState.value.copy(
                            myRooms = emptyList(),
                            isNewUser = true
                        )
                    }
                )
                
                // Load recent rooms (rooms user recently visited)
                loadRecentRooms()
                
                // Load following rooms (rooms from followed users)
                loadFollowingRooms()
                
                // Load video/music rooms
                loadVideoMusicRooms()
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading rooms", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    /**
     * Load recent rooms from backend.
     */
    private suspend fun loadRecentRooms() {
        try {
            val response = apiService.getRecentRooms()
            if (response.isSuccessful) {
                val data = response.body()
                val rooms = data?.data?.map { dto ->
                    RoomCard(
                        id = dto.id,
                        name = dto.name,
                        coverImage = dto.coverImage,
                        ownerName = dto.ownerName,
                        ownerAvatar = dto.ownerAvatar,
                        type = when (dto.type.lowercase()) {
                            "video" -> RoomType.VIDEO
                            "music" -> RoomType.MUSIC
                            else -> RoomType.VOICE
                        },
                        userCount = dto.userCount,
                        capacity = dto.capacity,
                        isLive = dto.isLive,
                        tags = dto.tags
                    )
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(recentRooms = rooms)
                Log.d(TAG, "Loaded ${rooms.size} recent rooms")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading recent rooms", e)
        }
    }
    
    /**
     * Load following rooms from backend.
     */
    private suspend fun loadFollowingRooms() {
        try {
            val response = apiService.getFollowingRooms()
            if (response.isSuccessful) {
                val data = response.body()
                val rooms = data?.data?.map { dto ->
                    RoomCard(
                        id = dto.id,
                        name = dto.name,
                        coverImage = dto.coverImage,
                        ownerName = dto.ownerName,
                        ownerAvatar = dto.ownerAvatar,
                        type = when (dto.type.lowercase()) {
                            "video" -> RoomType.VIDEO
                            "music" -> RoomType.MUSIC
                            else -> RoomType.VOICE
                        },
                        userCount = dto.userCount,
                        capacity = dto.capacity,
                        isLive = dto.isLive,
                        tags = dto.tags
                    )
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(followingRooms = rooms)
                Log.d(TAG, "Loaded ${rooms.size} following rooms")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading following rooms", e)
        }
    }
    
    /**
     * Load video/music rooms from backend.
     */
    private suspend fun loadVideoMusicRooms() {
        try {
            val response = apiService.getVideoMusicRooms()
            if (response.isSuccessful) {
                val data = response.body()
                val rooms = data?.data?.map { dto ->
                    RoomCard(
                        id = dto.id,
                        name = dto.name,
                        coverImage = dto.coverImage,
                        ownerName = dto.ownerName,
                        ownerAvatar = dto.ownerAvatar,
                        type = when (dto.type.lowercase()) {
                            "video" -> RoomType.VIDEO
                            "music" -> RoomType.MUSIC
                            else -> RoomType.VOICE
                        },
                        userCount = dto.userCount,
                        capacity = dto.capacity,
                        isLive = dto.isLive,
                        tags = dto.tags
                    )
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(videoMusicRooms = rooms)
                Log.d(TAG, "Loaded ${rooms.size} video/music rooms")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading video/music rooms", e)
        }
    }
    
    /**
     * Load banners from backend API.
     */
    private fun loadBanners() {
        viewModelScope.launch {
            try {
                val response = apiService.getBanners()
                if (response.isSuccessful) {
                    val banners = response.body()?.banners?.map { it.imageUrl } ?: emptyList()
                    _uiState.value = _uiState.value.copy(banners = banners)
                    Log.d(TAG, "Loaded ${banners.size} banners")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading banners", e)
            }
        }
    }
    
    /**
     * Refresh all data from the backend.
     */
    fun refresh() {
        loadAllRooms()
        loadUserInfo()
        loadBanners()
    }
    
    /**
     * Handle home icon click - either join user's room or show create dialog.
     */
    fun handleHomeIconClick(): HomeAction {
        return if (_uiState.value.isNewUser || _uiState.value.myRooms.isEmpty()) {
            HomeAction.ShowCreateRoom
        } else {
            HomeAction.JoinMyRoom(_uiState.value.myRooms.firstOrNull()?.id ?: "")
        }
    }
    
    /**
     * Create a new room via backend API.
     */
    fun createRoom(name: String, type: String, capacity: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isCreatingRoom = true)
                val response = apiService.createRoom(
                    com.aura.voicechat.data.model.CreateRoomRequest(
                        name = name,
                        type = type,
                        capacity = capacity
                    )
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d(TAG, "Room created successfully")
                    refresh() // Refresh to load new room
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = response.body()?.message ?: "Failed to create room"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating room", e)
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isCreatingRoom = false)
            }
        }
    }
    
    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * Sealed class for Home screen actions.
 */
sealed class HomeAction {
    object ShowCreateRoom : HomeAction()
    data class JoinMyRoom(val roomId: String) : HomeAction()
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val isCreatingRoom: Boolean = false,
    val currentUserId: String? = null,
    val isNewUser: Boolean = true,
    val myRooms: List<RoomCard> = emptyList(),
    val popularRooms: List<RoomCard> = emptyList(),
    val recentRooms: List<RoomCard> = emptyList(),
    val followingRooms: List<RoomCard> = emptyList(),
    val videoMusicRooms: List<RoomCard> = emptyList(),
    val banners: List<String> = emptyList(),
    val error: String? = null
)
