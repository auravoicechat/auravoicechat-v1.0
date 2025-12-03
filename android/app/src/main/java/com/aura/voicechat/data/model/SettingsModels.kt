package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Settings & Preferences Models
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// Message Privacy Enum
enum class MessagePrivacy {
    @SerializedName("everyone") EVERYONE,
    @SerializedName("friends_only") FRIENDS_ONLY,
    @SerializedName("no_one") NO_ONE
}

// Profile Visibility Enum
enum class ProfileVisibility {
    @SerializedName("public") PUBLIC,
    @SerializedName("friends_only") FRIENDS_ONLY,
    @SerializedName("private") PRIVATE
}

// User Preferences Data Model
data class UserPreferences(
    @SerializedName("pushNotificationsEnabled") val pushNotificationsEnabled: Boolean = true,
    @SerializedName("messageNotifications") val messageNotifications: Boolean = true,
    @SerializedName("giftNotifications") val giftNotifications: Boolean = true,
    @SerializedName("followNotifications") val followNotifications: Boolean = true,
    @SerializedName("soundEnabled") val soundEnabled: Boolean = true,
    @SerializedName("vibrationEnabled") val vibrationEnabled: Boolean = true,
    @SerializedName("showOnlineStatus") val showOnlineStatus: Boolean = true,
    @SerializedName("allowMessagesFrom") val allowMessagesFrom: MessagePrivacy = MessagePrivacy.EVERYONE,
    @SerializedName("profileVisibility") val profileVisibility: ProfileVisibility = ProfileVisibility.PUBLIC
)

// API Request to change phone number
data class ChangePhoneRequest(
    @SerializedName("newPhone") val newPhone: String,
    @SerializedName("otp") val otp: String
)

// API Response for settings
data class SettingsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("preferences") val preferences: UserPreferences
)
