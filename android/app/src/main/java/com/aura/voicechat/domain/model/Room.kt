package com.aura.voicechat.domain.model

/**
 * Room domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Voice/Video rooms with 8/16 seat layouts
 */
data class Room(
    val id: String,
    val name: String,
    val coverImage: String?,
    val ownerId: String,
    val ownerName: String,
    val ownerAvatar: String?,
    val type: RoomType,
    val mode: RoomMode,
    val capacity: Int,
    val currentUsers: Int,
    val isLocked: Boolean,
    val tags: List<String>,
    val seats: List<Seat>,
    val createdAt: Long
)

enum class RoomType {
    VOICE,
    VIDEO,
    MUSIC
}

enum class RoomMode {
    FREE,
    VIP_ONLY,
    INVITE_ONLY
}

data class Seat(
    val position: Int,
    val userId: String?,
    val userName: String?,
    val userAvatar: String?,
    val userLevel: Int?,
    val userVip: Int?,
    val isMuted: Boolean,
    val isLocked: Boolean,
    val effects: List<SeatEffect>
)

data class SeatEffect(
    val type: String,
    val id: String,
    val expiresAt: Long?
)

/**
 * Room card for home screen display
 */
data class RoomCard(
    val id: String,
    val name: String,
    val coverImage: String?,
    val ownerName: String,
    val ownerAvatar: String?,
    val type: RoomType,
    val userCount: Int,
    val capacity: Int,
    val isLive: Boolean,
    val tags: List<String>
)
