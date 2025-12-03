package com.aura.voicechat.ui.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.LiveStream
import com.aura.voicechat.data.model.StreamCategory
import com.aura.voicechat.data.repository.LiveStreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Live Stream ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class LiveStreamViewModel @Inject constructor(
    private val liveStreamRepository: LiveStreamRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<LiveStreamUiState>(LiveStreamUiState.Loading)
    val uiState: StateFlow<LiveStreamUiState> = _uiState.asStateFlow()
    
    private val _streams = MutableStateFlow<List<LiveStream>>(emptyList())
    val streams: StateFlow<List<LiveStream>> = _streams.asStateFlow()
    
    private val _currentStream = MutableStateFlow<LiveStream?>(null)
    val currentStream: StateFlow<LiveStream?> = _currentStream.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<StreamCategory?>(null)
    val selectedCategory: StateFlow<StreamCategory?> = _selectedCategory.asStateFlow()
    
    init {
        loadLiveStreams()
    }
    
    fun loadLiveStreams(category: StreamCategory? = null) {
        viewModelScope.launch {
            _uiState.value = LiveStreamUiState.Loading
            _selectedCategory.value = category
            
            liveStreamRepository.getLiveStreams(category).collect { result ->
                result.onSuccess { streamList ->
                    _streams.value = streamList
                    _uiState.value = if (streamList.isEmpty()) {
                        LiveStreamUiState.Empty
                    } else {
                        LiveStreamUiState.Success
                    }
                }.onFailure { error ->
                    _uiState.value = LiveStreamUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }
    
    fun loadStreamDetails(streamId: String) {
        viewModelScope.launch {
            liveStreamRepository.getLiveStreamDetails(streamId).collect { result ->
                result.onSuccess { stream ->
                    _currentStream.value = stream
                }.onFailure { error ->
                    _uiState.value = LiveStreamUiState.Error(error.message ?: "Failed to load stream")
                }
            }
        }
    }
    
    fun startStream(title: String, category: StreamCategory, privacy: String) {
        viewModelScope.launch {
            _uiState.value = LiveStreamUiState.Loading
            
            liveStreamRepository.startLiveStream(title, category, privacy).collect { result ->
                result.onSuccess { stream ->
                    _currentStream.value = stream
                    _uiState.value = LiveStreamUiState.StreamStarted
                }.onFailure { error ->
                    _uiState.value = LiveStreamUiState.Error(error.message ?: "Failed to start stream")
                }
            }
        }
    }
    
    fun stopStream(streamId: String) {
        viewModelScope.launch {
            liveStreamRepository.stopLiveStream(streamId).collect { result ->
                result.onSuccess {
                    _currentStream.value = null
                    _uiState.value = LiveStreamUiState.StreamStopped
                }.onFailure { error ->
                    _uiState.value = LiveStreamUiState.Error(error.message ?: "Failed to stop stream")
                }
            }
        }
    }
    
    fun refreshStreams() {
        loadLiveStreams(_selectedCategory.value)
    }
}

sealed class LiveStreamUiState {
    object Loading : LiveStreamUiState()
    object Success : LiveStreamUiState()
    object Empty : LiveStreamUiState()
    object StreamStarted : LiveStreamUiState()
    object StreamStopped : LiveStreamUiState()
    data class Error(val message: String) : LiveStreamUiState()
}
