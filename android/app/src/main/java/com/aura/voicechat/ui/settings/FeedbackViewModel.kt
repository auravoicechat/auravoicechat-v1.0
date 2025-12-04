package com.aura.voicechat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Feedback Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(FeedbackState())
    val state: StateFlow<FeedbackState> = _state.asStateFlow()
    
    fun submitFeedback(type: String, subject: String, description: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSubmitting = true, error = null)
            
            repository.submitFeedback(
                type = type,
                subject = subject,
                description = description,
                screenshots = _state.value.attachments
            ).fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        submitSuccess = true
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        error = error.message ?: "Failed to submit feedback"
                    )
                }
            )
        }
    }
    
    fun addAttachment(uri: String) {
        val currentAttachments = _state.value.attachments.toMutableList()
        currentAttachments.add(uri)
        _state.value = _state.value.copy(attachments = currentAttachments)
    }
    
    fun removeAttachment(uri: String) {
        val currentAttachments = _state.value.attachments.toMutableList()
        currentAttachments.remove(uri)
        _state.value = _state.value.copy(attachments = currentAttachments)
    }
}

data class FeedbackState(
    val attachments: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false,
    val error: String? = null
)
