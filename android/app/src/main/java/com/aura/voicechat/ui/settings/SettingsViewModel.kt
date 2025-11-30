package com.aura.voicechat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Settings ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        _uiState.value = SettingsUiState(
            pushNotifications = true,
            giftNotifications = true,
            messageNotifications = true,
            noiseCancellation = true,
            autoPlayAnimations = true,
            language = "English",
            cacheSize = 125,
            appVersion = "2.0.0",
            blockedCount = 3
        )
    }
    
    fun togglePushNotifications() {
        _uiState.value = _uiState.value.copy(
            pushNotifications = !_uiState.value.pushNotifications
        )
    }
    
    fun toggleGiftNotifications() {
        _uiState.value = _uiState.value.copy(
            giftNotifications = !_uiState.value.giftNotifications
        )
    }
    
    fun toggleMessageNotifications() {
        _uiState.value = _uiState.value.copy(
            messageNotifications = !_uiState.value.messageNotifications
        )
    }
    
    fun toggleNoiseCancellation() {
        _uiState.value = _uiState.value.copy(
            noiseCancellation = !_uiState.value.noiseCancellation
        )
    }
    
    fun toggleAutoPlayAnimations() {
        _uiState.value = _uiState.value.copy(
            autoPlayAnimations = !_uiState.value.autoPlayAnimations
        )
    }
    
    fun showLanguageDialog() {
        _uiState.value = _uiState.value.copy(showLanguageDialog = true)
    }
    
    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(
            language = language,
            showLanguageDialog = false
        )
    }
    
    fun clearCache() {
        viewModelScope.launch {
            // Clear cache logic
            _uiState.value = _uiState.value.copy(cacheSize = 0)
        }
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            // Delete account logic
        }
    }
}

data class SettingsUiState(
    val pushNotifications: Boolean = true,
    val giftNotifications: Boolean = true,
    val messageNotifications: Boolean = true,
    val noiseCancellation: Boolean = true,
    val autoPlayAnimations: Boolean = true,
    val language: String = "English",
    val cacheSize: Int = 0,
    val appVersion: String = "1.0.0",
    val blockedCount: Int = 0,
    val showLanguageDialog: Boolean = false
)
