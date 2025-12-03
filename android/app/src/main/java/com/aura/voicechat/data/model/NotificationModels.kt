package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Notification Models
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// Notification Type Enum
enum class NotificationType {
    @SerializedName("mention") MENTION,
    @SerializedName("gift") GIFT,
    @SerializedName("follow") FOLLOW,
    @SerializedName("system") SYSTEM,
    @SerializedName("room_invite") ROOM_INVITE,
    @SerializedName("cp_request") CP_REQUEST,
    @SerializedName("family_invite") FAMILY_INVITE,
    @SerializedName("event") EVENT,
    @SerializedName("daily_reward") DAILY_REWARD
}

// App Notification Data Model
data class AppNotification(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: NotificationType,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("deepLink") val deepLink: String?,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("createdAt") val createdAt: Long
)

// API Response for notifications list
data class NotificationsListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("notifications") val notifications: List<AppNotification>,
    @SerializedName("unreadCount") val unreadCount: Int
)
