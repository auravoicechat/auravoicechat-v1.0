package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.LiveStream
import com.aura.voicechat.data.model.StartStreamRequest
import com.aura.voicechat.data.model.StreamCategory
import com.aura.voicechat.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Live Streaming Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class LiveStreamRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * Get all live streams, optionally filtered by category
     */
    fun getLiveStreams(category: StreamCategory? = null): Flow<Result<List<LiveStream>>> = flow {
        try {
            val response = apiService.getLiveStreams(category?.name?.lowercase())
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!.streams))
            } else {
                emit(Result.failure(Exception("Failed to fetch live streams: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Start a new live stream
     */
    fun startLiveStream(
        title: String,
        category: StreamCategory,
        privacy: String
    ): Flow<Result<LiveStream>> = flow {
        try {
            val request = StartStreamRequest(title, category, privacy)
            val response = apiService.startLiveStream(request)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Failed to start stream: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Stop a live stream
     */
    fun stopLiveStream(streamId: String): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.stopLiveStream(streamId)
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to stop stream: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get details of a specific live stream
     */
    fun getLiveStreamDetails(streamId: String): Flow<Result<LiveStream>> = flow {
        try {
            val response = apiService.getLiveStreamDetails(streamId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Failed to fetch stream details: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
