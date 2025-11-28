package com.aura.voicechat.ui.kyc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.model.KycStep
import com.aura.voicechat.domain.repository.KycRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * KYC ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles KYC verification flow: ID Card (front/back) + Selfie
 * NO utility bills required
 */
@HiltViewModel
class KycViewModel @Inject constructor(
    private val kycRepository: KycRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(KycUiState())
    val uiState: StateFlow<KycUiState> = _uiState.asStateFlow()
    
    fun setIdCardFront(uri: String) {
        _uiState.value = _uiState.value.copy(
            idCardFrontUri = uri.ifEmpty { null }
        )
    }
    
    fun setIdCardBack(uri: String) {
        _uiState.value = _uiState.value.copy(
            idCardBackUri = uri.ifEmpty { null }
        )
    }
    
    fun setSelfie(uri: String) {
        _uiState.value = _uiState.value.copy(
            selfieUri = uri.ifEmpty { null }
        )
    }
    
    fun nextStep() {
        val currentStep = _uiState.value.currentStep
        val nextStep = when (currentStep) {
            KycStep.ID_CARD_FRONT -> KycStep.ID_CARD_BACK
            KycStep.ID_CARD_BACK -> KycStep.SELFIE_CAPTURE
            KycStep.SELFIE_CAPTURE -> KycStep.LIVENESS_CHECK
            KycStep.LIVENESS_CHECK -> KycStep.REVIEW_SUBMIT
            KycStep.REVIEW_SUBMIT -> KycStep.COMPLETE
            KycStep.COMPLETE -> KycStep.COMPLETE
        }
        _uiState.value = _uiState.value.copy(currentStep = nextStep)
    }
    
    fun previousStep() {
        val currentStep = _uiState.value.currentStep
        val prevStep = when (currentStep) {
            KycStep.ID_CARD_FRONT -> KycStep.ID_CARD_FRONT
            KycStep.ID_CARD_BACK -> KycStep.ID_CARD_FRONT
            KycStep.SELFIE_CAPTURE -> KycStep.ID_CARD_BACK
            KycStep.LIVENESS_CHECK -> KycStep.SELFIE_CAPTURE
            KycStep.REVIEW_SUBMIT -> KycStep.LIVENESS_CHECK
            KycStep.COMPLETE -> KycStep.REVIEW_SUBMIT
        }
        _uiState.value = _uiState.value.copy(currentStep = prevStep)
    }
    
    fun performLivenessCheck() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingLiveness = true)
            try {
                // Simulate liveness check
                delay(2000)
                _uiState.value = _uiState.value.copy(
                    isCheckingLiveness = false,
                    livenessCheckPassed = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCheckingLiveness = false,
                    livenessCheckPassed = false,
                    error = e.message
                )
            }
        }
    }
    
    fun submitKyc() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            try {
                val state = _uiState.value
                require(state.idCardFrontUri != null) { "ID card front is required" }
                require(state.idCardBackUri != null) { "ID card back is required" }
                require(state.selfieUri != null) { "Selfie is required" }
                
                // Simulate API call
                delay(2000)
                
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    currentStep = KycStep.COMPLETE,
                    isComplete = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = e.message
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class KycUiState(
    val currentStep: KycStep = KycStep.ID_CARD_FRONT,
    val idCardFrontUri: String? = null,
    val idCardBackUri: String? = null,
    val selfieUri: String? = null,
    val isCheckingLiveness: Boolean = false,
    val livenessCheckPassed: Boolean? = null,
    val isSubmitting: Boolean = false,
    val isComplete: Boolean = false,
    val error: String? = null
)
