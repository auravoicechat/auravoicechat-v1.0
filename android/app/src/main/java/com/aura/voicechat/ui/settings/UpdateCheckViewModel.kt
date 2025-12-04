package com.aura.voicechat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.BuildConfig
import com.aura.voicechat.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Update Check Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class UpdateCheckViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(UpdateCheckState())
    val state: StateFlow<UpdateCheckState> = _state.asStateFlow()
    
    init {
        _state.value = _state.value.copy(
            currentVersion = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE
        )
    }
    
    fun checkForUpdates() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isChecking = true, error = null)
            
            repository.checkUpdate().fold(
                onSuccess = { response ->
                    _state.value = _state.value.copy(
                        isChecking = false,
                        updateAvailable = response.updateAvailable,
                        latestVersion = response.version,
                        forceUpdate = response.forceUpdate,
                        downloadUrl = response.downloadUrl,
                        releaseNotes = response.releaseNotes
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isChecking = false,
                        error = error.message ?: "Failed to check for updates"
                    )
                }
            )
        }
    }
    
    fun downloadUpdate() {
        // TODO: Implement download logic
        // For now, open the download URL in browser
        val downloadUrl = _state.value.downloadUrl
        if (downloadUrl != null) {
            // Use Android's DownloadManager or open in browser
        }
    }
}

data class UpdateCheckState(
    val currentVersion: String = "1.0.0",
    val versionCode: Int = 1,
    val isChecking: Boolean = false,
    val updateAvailable: Boolean = false,
    val latestVersion: String = "",
    val forceUpdate: Boolean = false,
    val downloadUrl: String? = null,
    val releaseNotes: List<String> = emptyList(),
    val error: String? = null
)
