package com.aura.voicechat.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.AppNotification
import com.aura.voicechat.data.model.NotificationType
import com.aura.voicechat.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Notification ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    
    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    init {
        loadNotifications()
        observeUnreadCount()
    }
    
    fun loadNotifications(type: NotificationType? = null) {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading
            
            notificationRepository.getNotifications(type).collect { result ->
                result.onSuccess { notifs ->
                    _notifications.value = notifs
                    _uiState.value = if (notifs.isEmpty()) {
                        NotificationUiState.Empty
                    } else {
                        NotificationUiState.Success
                    }
                }.onFailure { error ->
                    _uiState.value = NotificationUiState.Error(error.message ?: "Failed to load notifications")
                }
            }
        }
    }
    
    private fun observeUnreadCount() {
        viewModelScope.launch {
            notificationRepository.getUnreadCount().collect { count ->
                _unreadCount.value = count
            }
        }
    }
    
    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId).collect { /* Handle result */ }
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            notificationRepository.markAllAsRead().collect { result ->
                result.onSuccess {
                    loadNotifications()
                }
            }
        }
    }
    
    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId).collect { result ->
                result.onSuccess {
                    _notifications.value = _notifications.value.filter { it.id != notificationId }
                }
            }
        }
    }
}

sealed class NotificationUiState {
    object Loading : NotificationUiState()
    object Success : NotificationUiState()
    object Empty : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
}
