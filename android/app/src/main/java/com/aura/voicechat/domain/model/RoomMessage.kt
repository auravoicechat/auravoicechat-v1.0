package com.aura.voicechat.domain.model

/**
 * RoomMessage domain models for chat messages in rooms
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Sealed class hierarchy for different message types in a room
 */
sealed class RoomMessage(
    open val id: String,
    open val timestamp: Long
) {
    /**
     * System message - announcements, user join/leave, etc.
     */
    data class SystemMessage(
        override val id: String,
        override val timestamp: Long,
        val text: String
    ) : RoomMessage(id, timestamp)
    
    /**
     * User text message
     */
    data class UserMessage(
        override val id: String,
        override val timestamp: Long,
        val user: User,
        val text: String
    ) : RoomMessage(id, timestamp)
    
    /**
     * Gift message - user sent a gift to another user
     */
    data class GiftMessage(
        override val id: String,
        override val timestamp: Long,
        val sender: User,
        val receiver: User,
        val gift: Gift,
        val quantity: Int
    ) : RoomMessage(id, timestamp)
}
