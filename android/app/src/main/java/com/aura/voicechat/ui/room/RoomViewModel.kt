package com.aura.voicechat.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.model.Room
import com.aura.voicechat.domain.model.RoomMode
import com.aura.voicechat.domain.model.RoomType
import com.aura.voicechat.domain.model.Seat
import com.aura.voicechat.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Room ViewModel for voice/video rooms
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()
    
    fun loadRoom(roomId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Mock room data
                val room = Room(
                    id = roomId,
                    name = "Music Lounge 🎵",
                    coverImage = null,
                    ownerId = "owner_1",
                    ownerName = "DJ Mike",
                    ownerAvatar = null,
                    type = RoomType.MUSIC,
                    mode = RoomMode.FREE,
                    capacity = 8,
                    currentUsers = 5,
                    isLocked = false,
                    tags = listOf("Music", "Chill"),
                    seats = generateMockSeats(8),
                    createdAt = System.currentTimeMillis()
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    room = room
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun onSeatClick(position: Int) {
        // Handle seat click - request to join or view user profile
        viewModelScope.launch {
            val room = _uiState.value.room ?: return@launch
            val seat = room.seats.getOrNull(position) ?: return@launch
            
            if (seat.userId == null && !seat.isLocked) {
                // Request to join seat
                joinSeat(position)
            } else if (seat.userId != null) {
                // Show user profile
                _uiState.value = _uiState.value.copy(
                    selectedUserId = seat.userId
                )
            }
        }
    }
    
    private fun joinSeat(position: Int) {
        viewModelScope.launch {
            try {
                // API call to join seat
                val room = _uiState.value.room ?: return@launch
                val updatedSeats = room.seats.toMutableList()
                updatedSeats[position] = updatedSeats[position].copy(
                    userId = "current_user",
                    userName = "You",
                    isMuted = true
                )
                
                _uiState.value = _uiState.value.copy(
                    room = room.copy(seats = updatedSeats),
                    currentSeatPosition = position
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun leaveSeat() {
        viewModelScope.launch {
            val position = _uiState.value.currentSeatPosition ?: return@launch
            val room = _uiState.value.room ?: return@launch
            
            val updatedSeats = room.seats.toMutableList()
            updatedSeats[position] = updatedSeats[position].copy(
                userId = null,
                userName = null,
                isMuted = false
            )
            
            _uiState.value = _uiState.value.copy(
                room = room.copy(seats = updatedSeats),
                currentSeatPosition = null
            )
        }
    }
    
    fun sendGift(giftId: String) {
        viewModelScope.launch {
            try {
                // API call to send gift
                _uiState.value = _uiState.value.copy(
                    message = "Gift sent successfully!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun toggleMute() {
        viewModelScope.launch {
            val position = _uiState.value.currentSeatPosition ?: return@launch
            val room = _uiState.value.room ?: return@launch
            
            val updatedSeats = room.seats.toMutableList()
            val currentSeat = updatedSeats[position]
            updatedSeats[position] = currentSeat.copy(isMuted = !currentSeat.isMuted)
            
            _uiState.value = _uiState.value.copy(
                room = room.copy(seats = updatedSeats),
                isMuted = !currentSeat.isMuted
            )
        }
    }
    
    private fun generateMockSeats(count: Int): List<Seat> {
        return (0 until count).map { position ->
            when (position) {
                0 -> Seat(
                    position = position,
                    userId = "user_1",
                    userName = "Alice",
                    userAvatar = null,
                    userLevel = 25,
                    userVip = 3,
                    isMuted = false,
                    isLocked = false,
                    effects = emptyList()
                )
                2 -> Seat(
                    position = position,
                    userId = "user_2",
                    userName = "Bob",
                    userAvatar = null,
                    userLevel = 18,
                    userVip = null,
                    isMuted = true,
                    isLocked = false,
                    effects = emptyList()
                )
                5 -> Seat(
                    position = position,
                    userId = null,
                    userName = null,
                    userAvatar = null,
                    userLevel = null,
                    userVip = null,
                    isMuted = false,
                    isLocked = true,
                    effects = emptyList()
                )
                else -> Seat(
                    position = position,
                    userId = null,
                    userName = null,
                    userAvatar = null,
                    userLevel = null,
                    userVip = null,
                    isMuted = false,
                    isLocked = false,
                    effects = emptyList()
                )
            }
        }
    }
}

data class RoomUiState(
    val isLoading: Boolean = false,
    val room: Room? = null,
    val currentSeatPosition: Int? = null,
    val isMuted: Boolean = true,
    val selectedUserId: String? = null,
    val message: String? = null,
    val error: String? = null
)
