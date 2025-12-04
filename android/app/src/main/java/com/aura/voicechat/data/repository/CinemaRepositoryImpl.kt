package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.CinemaSession
import com.aura.voicechat.data.model.StartCinemaRequest
import com.aura.voicechat.data.model.SyncCinemaRequest
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.CinemaRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Cinema operations
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class CinemaRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CinemaRepository {
    
    override suspend fun startCinema(
        roomId: String,
        videoUrl: String,
        videoTitle: String,
        videoDuration: Long
    ): Result<CinemaSession> {
        return try {
            val request = StartCinemaRequest(
                videoUrl = videoUrl,
                videoTitle = videoTitle,
                videoDuration = videoDuration
            )
            
            val response = apiService.startCinema(roomId, request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to start cinema: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCinemaSession(roomId: String): Result<CinemaSession> {
        return try {
            val response = apiService.getCinemaSession(roomId)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get cinema session: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncPlayback(
        roomId: String,
        currentPosition: Long,
        isPlaying: Boolean
    ): Result<Unit> {
        return try {
            val request = SyncCinemaRequest(
                currentPosition = currentPosition,
                isPlaying = isPlaying
            )
            
            val response = apiService.syncCinema(roomId, request)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to sync playback: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun stopCinema(roomId: String): Result<Unit> {
        return try {
            val response = apiService.stopCinema(roomId)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to stop cinema: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
