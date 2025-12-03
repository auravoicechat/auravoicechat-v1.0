package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Live Streaming Models
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// Enum for stream categories
enum class StreamCategory {
    @SerializedName("chat") CHAT,
    @SerializedName("music") MUSIC,
    @SerializedName("gaming") GAMING,
    @SerializedName("talent") TALENT,
    @SerializedName("party") PARTY
}

// Live Stream Data Model
data class LiveStream(
    @SerializedName("id") val id: String,
    @SerializedName("hostId") val hostId: String,
    @SerializedName("hostName") val hostName: String,
    @SerializedName("hostAvatarUrl") val hostAvatarUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("viewerCount") val viewerCount: Int,
    @SerializedName("category") val category: StreamCategory,
    @SerializedName("isLive") val isLive: Boolean,
    @SerializedName("startedAt") val startedAt: Long,
    @SerializedName("streamUrl") val streamUrl: String?
)

// API Request to start a stream
data class StartStreamRequest(
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: StreamCategory,
    @SerializedName("privacy") val privacy: String // "public", "private", "friends_only"
)

// API Response for live streams list
data class LiveStreamsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("streams") val streams: List<LiveStream>
)
