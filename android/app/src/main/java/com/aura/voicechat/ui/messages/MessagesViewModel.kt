package com.aura.voicechat.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Messages ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class MessagesViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()
    
    init {
        loadMessages()
    }
    
    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = MessagesUiState(
                conversations = listOf(
                    Conversation("conv1", "StarLight", null, "Hey! How are you?", "2 min ago", 2, true),
                    Conversation("conv2", "DragonKing", null, "Thanks for the gift! 🎁", "1 hour ago", 0, false),
                    Conversation("conv3", "MoonRider", null, "See you in the room later!", "3 hours ago", 1, true),
                    Conversation("conv4", "PhoenixFire", null, "Great performance today!", "Yesterday", 0, false),
                    Conversation("conv5", "IceQueen", null, "Let's team up", "2 days ago", 0, false)
                ),
                notifications = listOf(
                    NotificationItem("notif1", "gift", "Gift Received", "DragonKing sent you Rose x10", "5 min ago", false),
                    NotificationItem("notif2", "follow", "New Follower", "StarLight started following you", "1 hour ago", false),
                    NotificationItem("notif3", "like", "Profile Liked", "MoonRider liked your profile", "2 hours ago", true),
                    NotificationItem("notif4", "friend", "Friend Request", "PhoenixFire wants to be your friend", "5 hours ago", false),
                    NotificationItem("notif5", "gift", "Gift Received", "IceQueen sent you Diamond x5", "Yesterday", true)
                ),
                systemMessages = listOf(
                    SystemMessage("sys1", "Weekly Maintenance", "Server will be under maintenance on Sunday 3AM-5AM UTC", "Today"),
                    SystemMessage("sys2", "New Event!", "Weekly Party Star event is now live! Join now for exciting rewards.", "Yesterday"),
                    SystemMessage("sys3", "App Update", "Version 2.0 is now available with new features and improvements", "3 days ago")
                ),
                unreadMessages = 3,
                unreadNotifications = 3,
                unreadSystem = 1
            )
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            val updatedConversations = _uiState.value.conversations.map { it.copy(unreadCount = 0) }
            val updatedNotifications = _uiState.value.notifications.map { it.copy(isRead = true) }
            
            _uiState.value = _uiState.value.copy(
                conversations = updatedConversations,
                notifications = updatedNotifications,
                unreadMessages = 0,
                unreadNotifications = 0,
                unreadSystem = 0,
                unreadCount = 0
            )
        }
    }
    
    fun handleNotification(notificationId: String) {
        viewModelScope.launch {
            val updatedNotifications = _uiState.value.notifications.map { notification ->
                if (notification.id == notificationId) notification.copy(isRead = true) else notification
            }
            val unreadCount = updatedNotifications.count { !it.isRead }
            
            _uiState.value = _uiState.value.copy(
                notifications = updatedNotifications,
                unreadNotifications = unreadCount
            )
        }
    }
}

data class MessagesUiState(
    val isLoading: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val notifications: List<NotificationItem> = emptyList(),
    val systemMessages: List<SystemMessage> = emptyList(),
    val unreadMessages: Int = 0,
    val unreadNotifications: Int = 0,
    val unreadSystem: Int = 0,
    val unreadCount: Int = 0
)
