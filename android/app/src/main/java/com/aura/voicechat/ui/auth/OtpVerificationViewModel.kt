package com.aura.voicechat.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * OTP Verification ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OtpVerificationUiState())
    val uiState: StateFlow<OtpVerificationUiState> = _uiState.asStateFlow()
    
    init {
        startResendCooldown()
    }
    
    fun verifyOtp(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = authRepository.verifyOtp(phoneNumber, otp)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isVerified = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Invalid verification code. Please try again."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Verification failed"
                )
            }
        }
    }
    
    fun resendOtp(phoneNumber: String) {
        viewModelScope.launch {
            try {
                authRepository.sendOtp(phoneNumber)
                startResendCooldown()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to resend code"
                )
            }
        }
    }
    
    private fun startResendCooldown() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(resendCooldown = 30)
            while (_uiState.value.resendCooldown > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    resendCooldown = _uiState.value.resendCooldown - 1
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class OtpVerificationUiState(
    val isLoading: Boolean = false,
    val isVerified: Boolean = false,
    val resendCooldown: Int = 30,
    val error: String? = null
)
