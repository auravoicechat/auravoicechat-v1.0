package com.aura.voicechat.ui.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Register ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles user registration with validation logic.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "RegisterViewModel"
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_USERNAME_LENGTH = 20
        private const val MIN_DISPLAY_NAME_LENGTH = 2
        private const val MAX_DISPLAY_NAME_LENGTH = 30
    }
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    /**
     * Validate username format and availability.
     * Username must be 3-20 characters, alphanumeric with underscores.
     */
    fun validateUsername(username: String): Boolean {
        val usernameError = when {
            username.isBlank() -> "Username cannot be empty"
            username.length < MIN_USERNAME_LENGTH -> "Username must be at least $MIN_USERNAME_LENGTH characters"
            username.length > MAX_USERNAME_LENGTH -> "Username must be at most $MAX_USERNAME_LENGTH characters"
            !username.matches(Regex("^[a-zA-Z0-9_]+$")) -> "Username can only contain letters, numbers, and underscores"
            else -> null
        }
        
        _uiState.value = _uiState.value.copy(usernameError = usernameError)
        return usernameError == null
    }
    
    /**
     * Validate display name format.
     * Display name must be 2-30 characters.
     */
    fun validateDisplayName(displayName: String): Boolean {
        val displayNameError = when {
            displayName.isBlank() -> "Display name cannot be empty"
            displayName.length < MIN_DISPLAY_NAME_LENGTH -> "Display name must be at least $MIN_DISPLAY_NAME_LENGTH characters"
            displayName.length > MAX_DISPLAY_NAME_LENGTH -> "Display name must be at most $MAX_DISPLAY_NAME_LENGTH characters"
            else -> null
        }
        
        _uiState.value = _uiState.value.copy(displayNameError = displayNameError)
        return displayNameError == null
    }
    
    /**
     * Register a new user with the provided details.
     */
    fun register(
        username: String,
        displayName: String,
        dob: String,
        gender: String,
        avatarUri: Uri?
    ) {
        // Validate inputs
        val isUsernameValid = validateUsername(username)
        val isDisplayNameValid = validateDisplayName(displayName)
        
        if (!isUsernameValid || !isDisplayNameValid) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // TODO: Implement actual registration API call through AuthRepository
                // For now, simulate registration
                Log.d(TAG, "Registering user: $username, $displayName, $dob, $gender")
                
                // Simulate network delay
                kotlinx.coroutines.delay(1500)
                
                // Simulate successful registration
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true
                )
                
                Log.i(TAG, "Registration successful for username: $username")
            } catch (e: Exception) {
                Log.e(TAG, "Registration failed", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Registration failed. Please try again."
                )
            }
        }
    }
    
    /**
     * Clear the current error message.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Clear username error.
     */
    fun clearUsernameError() {
        _uiState.value = _uiState.value.copy(usernameError = null)
    }
    
    /**
     * Clear display name error.
     */
    fun clearDisplayNameError() {
        _uiState.value = _uiState.value.copy(displayNameError = null)
    }
}

/**
 * UI State for the Registration screen.
 */
data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
    val usernameError: String? = null,
    val displayNameError: String? = null
)
