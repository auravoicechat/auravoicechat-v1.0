package com.aura.voicechat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.UserPreferences
import com.aura.voicechat.data.repository.PreferencesRepository
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
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    private val _preferences = MutableStateFlow<UserPreferences?>(null)
    val preferences: StateFlow<UserPreferences?> = _preferences.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            preferencesRepository.getPreferences().collect { prefs ->
                _preferences.value = prefs
                _uiState.value = SettingsUiState(
                    pushNotifications = prefs.pushNotificationsEnabled,
                    giftNotifications = prefs.giftNotifications,
                    messageNotifications = prefs.messageNotifications,
                    noiseCancellation = true, // From local settings
                    autoPlayAnimations = true, // From local settings
                    language = "English", // From locale
                    cacheSize = 125, // From local cache
                    appVersion = "2.0.0",
                    blockedCount = 0 // From backend
                )
            }
        }
    }
    
    fun togglePushNotifications() {
        _preferences.value?.let { prefs ->
            val updated = prefs.copy(pushNotificationsEnabled = !prefs.pushNotificationsEnabled)
            updatePreferences(updated)
        }
    }
    
    fun toggleGiftNotifications() {
        _preferences.value?.let { prefs ->
            val updated = prefs.copy(giftNotifications = !prefs.giftNotifications)
            updatePreferences(updated)
        }
    }
    
    fun toggleMessageNotifications() {
        _preferences.value?.let { prefs ->
            val updated = prefs.copy(messageNotifications = !prefs.messageNotifications)
            updatePreferences(updated)
        }
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
    
    private fun updatePreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            preferencesRepository.updatePreferences(preferences).collect { result ->
                result.onSuccess {
                    loadSettings()
                }
            }
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
