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
 * ViewModel for Support Tickets
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class SupportTicketsViewModel @Inject constructor(
    // private val supportRepository: SupportRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(SupportTicketsState())
    val state: StateFlow<SupportTicketsState> = _state.asStateFlow()
    
    init {
        loadTickets()
    }
    
    private fun loadTickets() {
        viewModelScope.launch {
            // Sample data
            _state.value = _state.value.copy(
                myTickets = listOf(
                    SupportTicket(
                        id = "12345",
                        subject = "Cannot withdraw diamonds",
                        category = "Payment",
                        status = "IN_PROGRESS",
                        lastMessage = "Our team is reviewing your withdrawal request...",
                        lastUpdated = System.currentTimeMillis() - 3600000,
                        hasUnread = true
                    ),
                    SupportTicket(
                        id = "12344",
                        subject = "Account verification issue",
                        category = "Account",
                        status = "OPEN",
                        lastMessage = "I need help with account verification",
                        lastUpdated = System.currentTimeMillis() - 7200000,
                        hasUnread = false
                    )
                ),
                resolvedTickets = listOf(),
                hasActiveChat = false
            )
        }
    }
}

data class SupportTicketsState(
    val myTickets: List<SupportTicket> = emptyList(),
    val resolvedTickets: List<SupportTicket> = emptyList(),
    val hasActiveChat: Boolean = false
)
