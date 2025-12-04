package com.aura.voicechat.ui.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.Song
import com.aura.voicechat.data.model.Playlist
import com.aura.voicechat.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Music Player ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<MusicUiState>(MusicUiState.Idle)
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()
    
    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist: StateFlow<List<Song>> = _playlist.asStateFlow()
    
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _isShuffleEnabled = MutableStateFlow(false)
    val isShuffleEnabled: StateFlow<Boolean> = _isShuffleEnabled.asStateFlow()
    
    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()
    
    init {
        loadPlaylists()
        loadTrendingSongs()
    }
    
    fun loadTrendingSongs() {
        viewModelScope.launch {
            _uiState.value = MusicUiState.Loading
            musicRepository.getTrendingSongs().collect { result ->
                result.onSuccess { songs ->
                    _playlist.value = songs
                    _uiState.value = MusicUiState.Success
                }.onFailure { error ->
                    _uiState.value = MusicUiState.Error(error.message ?: "Failed to load songs")
                }
            }
        }
    }
    
    fun searchSongs(query: String) {
        viewModelScope.launch {
            _uiState.value = MusicUiState.Loading
            musicRepository.searchSongs(query).collect { result ->
                result.onSuccess { songs ->
                    _playlist.value = songs
                    _uiState.value = MusicUiState.Success
                }.onFailure { error ->
                    _uiState.value = MusicUiState.Error(error.message ?: "Search failed")
                }
            }
        }
    }
    
    fun loadPlaylists() {
        viewModelScope.launch {
            musicRepository.getPlaylists().collect { result ->
                result.onSuccess { lists ->
                    _playlists.value = lists
                }.onFailure { /* Handle error silently */ }
            }
        }
    }
    
    fun createPlaylist(name: String, songIds: List<String>) {
        viewModelScope.launch {
            musicRepository.createPlaylist(name, songIds).collect { result ->
                result.onSuccess { newPlaylist ->
                    _playlists.value = _playlists.value + newPlaylist
                    _uiState.value = MusicUiState.PlaylistCreated
                }.onFailure { error ->
                    _uiState.value = MusicUiState.Error(error.message ?: "Failed to create playlist")
                }
            }
        }
    }
    
    fun playSong(song: Song) {
        _currentSong.value = song
        _isPlaying.value = true
        // TODO: Integrate with MusicService to actually play the audio
    }
    
    fun pauseSong() {
        _isPlaying.value = false
        // TODO: Pause through MusicService
    }
    
    fun resumeSong() {
        _isPlaying.value = true
        // TODO: Resume through MusicService
    }
    
    fun playNext() {
        val currentIndex = _playlist.value.indexOfFirst { it.id == _currentSong.value?.id }
        if (currentIndex >= 0 && currentIndex < _playlist.value.size - 1) {
            playSong(_playlist.value[currentIndex + 1])
        } else if (_repeatMode.value == RepeatMode.ALL) {
            playSong(_playlist.value.first())
        }
    }
    
    fun playPrevious() {
        val currentIndex = _playlist.value.indexOfFirst { it.id == _currentSong.value?.id }
        if (currentIndex > 0) {
            playSong(_playlist.value[currentIndex - 1])
        } else if (_repeatMode.value == RepeatMode.ALL) {
            playSong(_playlist.value.last())
        }
    }
    
    fun toggleShuffle() {
        _isShuffleEnabled.value = !_isShuffleEnabled.value
        if (_isShuffleEnabled.value) {
            _playlist.value = _playlist.value.shuffled()
        }
    }
    
    fun toggleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }
    
    fun seekTo(position: Long) {
        _currentPosition.value = position
        // TODO: Seek through MusicService
    }
    
    fun updatePosition(position: Long) {
        _currentPosition.value = position
    }
}

sealed class MusicUiState {
    object Idle : MusicUiState()
    object Loading : MusicUiState()
    object Success : MusicUiState()
    object PlaylistCreated : MusicUiState()
    data class Error(val message: String) : MusicUiState()
}

enum class RepeatMode {
    OFF, ALL, ONE
}
