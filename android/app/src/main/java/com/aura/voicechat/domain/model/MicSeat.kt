package com.aura.voicechat.domain.model

/**
 * MicSeat domain model for Room voice seats
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Represents a microphone seat in a voice chat room
 */
data class MicSeat(
    val index: Int,
    val user: User? = null,
    val isMuted: Boolean = false,
    val isSpeaking: Boolean = false,
    val isLocked: Boolean = false
)
