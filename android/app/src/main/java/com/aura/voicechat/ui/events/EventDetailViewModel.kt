package com.aura.voicechat.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Event Detail Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(EventDetailState())
    val state: StateFlow<EventDetailState> = _state.asStateFlow()
    
    fun loadEventDetails(eventId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            repository.getEventDetails(eventId).fold(
                onSuccess = { response ->
                    val event = EventDetail(
                        id = response.id,
                        title = response.title,
                        description = response.description,
                        bannerUrl = response.bannerUrl,
                        startTime = response.startTime,
                        endTime = response.endTime,
                        type = response.type,
                        status = response.status,
                        rewards = response.rewards.map { dto ->
                            EventReward(
                                id = dto.id,
                                name = dto.name,
                                iconUrl = dto.iconUrl,
                                value = dto.value,
                                type = dto.type
                            )
                        },
                        rules = response.rules,
                        userProgress = response.userProgress?.let { progress ->
                            EventProgress(
                                currentValue = progress.currentValue,
                                targetValue = progress.targetValue,
                                rewardsEarned = progress.rewardsEarned
                            )
                        },
                        isParticipating = response.isParticipating
                    )
                    
                    _state.value = _state.value.copy(
                        event = event,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load event details"
                    )
                }
            )
        }
    }
    
    fun participateEvent() {
        viewModelScope.launch {
            val eventId = _state.value.event?.id ?: return@launch
            
            repository.participateEvent(eventId).fold(
                onSuccess = {
                    // Reload event details
                    loadEventDetails(eventId)
                },
                onFailure = { error ->
                    // Show error
                }
            )
        }
    }
    
    fun claimReward(rewardId: String) {
        viewModelScope.launch {
            val eventId = _state.value.event?.id ?: return@launch
            
            repository.claimEventReward(eventId, rewardId).fold(
                onSuccess = {
                    // Reload event details
                    loadEventDetails(eventId)
                },
                onFailure = { error ->
                    // Show error
                }
            )
        }
    }
}

data class EventDetailState(
    val event: EventDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
