package com.aura.voicechat.data.repository

import com.aura.voicechat.data.local.dao.SongDao
import com.aura.voicechat.data.local.dao.PlaylistDao
import com.aura.voicechat.data.local.dao.PlaylistSongDao
import com.aura.voicechat.data.local.entity.SongEntity
import com.aura.voicechat.data.local.entity.PlaylistEntity
import com.aura.voicechat.data.local.entity.PlaylistSongEntity
import com.aura.voicechat.data.model.Song
import com.aura.voicechat.data.model.Playlist
import com.aura.voicechat.data.model.CreatePlaylistRequest
import com.aura.voicechat.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Music Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class MusicRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao,
    private val playlistSongDao: PlaylistSongDao
) {
    
    /**
     * Search songs from API and cache locally
     */
    fun searchSongs(query: String? = null): Flow<Result<List<Song>>> = flow {
        try {
            val response = apiService.getSongs(query)
            if (response.isSuccessful && response.body() != null) {
                val songs = response.body()!!.songs
                // Cache songs locally
                songDao.insertSongs(songs.map { it.toEntity() })
                emit(Result.success(songs))
            } else {
                // Try to load from local cache if API fails
                val cachedSongs = if (query.isNullOrBlank()) {
                    songDao.getAllSongs()
                } else {
                    songDao.searchSongs(query)
                }
                cachedSongs.collect { entities ->
                    if (entities.isNotEmpty()) {
                        emit(Result.success(entities.map { it.toSong() }))
                    } else {
                        emit(Result.failure(Exception("Failed to fetch songs: ${response.message()}")))
                    }
                }
            }
        } catch (e: Exception) {
            // Try to load from local cache on exception
            val cachedSongs = if (query.isNullOrBlank()) {
                songDao.getAllSongs()
            } else {
                songDao.searchSongs(query)
            }
            cachedSongs.collect { entities ->
                if (entities.isNotEmpty()) {
                    emit(Result.success(entities.map { it.toSong() }))
                } else {
                    emit(Result.failure(e))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get trending songs
     */
    fun getTrendingSongs(): Flow<Result<List<Song>>> = flow {
        try {
            val response = apiService.getTrendingSongs()
            if (response.isSuccessful && response.body() != null) {
                val songs = response.body()!!.songs
                // Cache songs locally
                songDao.insertSongs(songs.map { it.toEntity() })
                emit(Result.success(songs))
            } else {
                emit(Result.failure(Exception("Failed to fetch trending songs: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get user playlists from API and cache
     */
    fun getPlaylists(): Flow<Result<List<Playlist>>> = flow {
        try {
            val response = apiService.getPlaylists()
            if (response.isSuccessful && response.body() != null) {
                val playlists = response.body()!!.playlists
                // Cache playlists locally
                playlistDao.insertPlaylists(playlists.map { it.toEntity() })
                emit(Result.success(playlists))
            } else {
                emit(Result.failure(Exception("Failed to fetch playlists: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Create a new playlist
     */
    fun createPlaylist(name: String, songIds: List<String>): Flow<Result<Playlist>> = flow {
        try {
            val request = CreatePlaylistRequest(name, songIds)
            val response = apiService.createPlaylist(request)
            if (response.isSuccessful && response.body() != null) {
                val playlist = response.body()!!
                // Cache playlist locally
                playlistDao.insertPlaylist(playlist.toEntity())
                emit(Result.success(playlist))
            } else {
                emit(Result.failure(Exception("Failed to create playlist: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get cached playlists from local database
     */
    fun getCachedPlaylists(userId: String): Flow<List<Playlist>> {
        return playlistDao.getUserPlaylists(userId)
            .map { entities -> entities.map { it.toPlaylist() } }
            .flowOn(Dispatchers.IO)
    }
    
    /**
     * Get songs in a playlist from local cache
     */
    fun getPlaylistSongs(playlistId: String): Flow<List<Song>> {
        return playlistSongDao.getPlaylistSongs(playlistId)
            .map { entities -> entities.map { it.toSong() } }
            .flowOn(Dispatchers.IO)
    }
    
    // Extension functions for entity mapping
    private fun Song.toEntity() = SongEntity(
        id = id,
        title = title,
        artist = artist,
        albumArt = albumArt,
        duration = duration,
        streamUrl = streamUrl
    )
    
    private fun SongEntity.toSong() = Song(
        id = id,
        title = title,
        artist = artist,
        albumArt = albumArt,
        duration = duration,
        streamUrl = streamUrl,
        isPlaying = false
    )
    
    private fun Playlist.toEntity() = PlaylistEntity(
        id = id,
        name = name,
        coverUrl = coverUrl,
        createdBy = createdBy,
        songCount = songs.size
    )
    
    private fun PlaylistEntity.toPlaylist() = Playlist(
        id = id,
        name = name,
        songs = emptyList(), // Songs loaded separately
        coverUrl = coverUrl,
        createdBy = createdBy
    )
}
