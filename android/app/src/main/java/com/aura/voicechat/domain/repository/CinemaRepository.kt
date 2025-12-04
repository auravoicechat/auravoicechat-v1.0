package com.aura.voicechat.domain.repository

import com.aura.voicechat.data.model.CinemaSession

/**
 * Cinema Repository Interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface CinemaRepository {
    suspend fun startCinema(
        roomId: String,
        videoUrl: String,
        videoTitle: String,
        videoDuration: Long
    ): Result<CinemaSession>
    
    suspend fun getCinemaSession(roomId: String): Result<CinemaSession>
    
    suspend fun syncPlayback(
        roomId: String,
        currentPosition: Long,
        isPlaying: Boolean
    ): Result<Unit>
    
    suspend fun stopCinema(roomId: String): Result<Unit>
}
