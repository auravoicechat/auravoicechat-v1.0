package com.aura.voicechat.ui.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Live Chat
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class LiveChatViewModel @Inject constructor(
    // private val supportRepository: SupportRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(LiveChatState())
    val state: StateFlow<LiveChatState> = _state.asStateFlow()
    
    fun loadChat(ticketId: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                ticketId = ticketId,
                supportAgentName = "Sarah (Support)",
                agentOnline = true,
                isLoading = false
            )
            
            // Load existing messages if any
            // val messages = supportRepository.getChatMessages(ticketId)
        }
    }
    
    fun sendMessage(content: String) {
        viewModelScope.launch {
            val newMessage = ChatMessage(
                id = System.currentTimeMillis().toString(),
                senderId = "current_user",
                content = content,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            
            _state.value = _state.value.copy(
                messages = _state.value.messages + newMessage
            )
            
            // Send to backend
            // supportRepository.sendChatMessage(ticketId, content)
        }
    }
    
    fun onTyping() {
        viewModelScope.launch {
            // Notify backend that user is typing
            // supportRepository.notifyTyping()
        }
    }
}

data class LiveChatState(
    val ticketId: String? = null,
    val messages: List<ChatMessage> = emptyList(),
    val supportAgentName: String? = null,
    val agentOnline: Boolean = false,
    val agentTyping: Boolean = false,
    val isLoading: Boolean = false
)
