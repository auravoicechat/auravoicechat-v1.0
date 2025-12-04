package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Music Player Models
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// Song Data Model
data class Song(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("albumArt") val albumArt: String,
    @SerializedName("duration") val duration: Long,
    @SerializedName("streamUrl") val streamUrl: String,
    @SerializedName("isPlaying") val isPlaying: Boolean = false
)

// Playlist Data Model
data class Playlist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("songs") val songs: List<Song>,
    @SerializedName("coverUrl") val coverUrl: String,
    @SerializedName("createdBy") val createdBy: String
)

// API Request to create a playlist
data class CreatePlaylistRequest(
    @SerializedName("name") val name: String,
    @SerializedName("songIds") val songIds: List<String>
)

// API Response for songs list
data class SongsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("songs") val songs: List<Song>
)

// API Response for playlists list
data class PlaylistsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("playlists") val playlists: List<Playlist>
)
