package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.RankingUser
import com.aura.voicechat.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Leaderboard Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class LeaderboardRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * Get leaderboard by type and period
     * @param type: "wealth", "charm", "level"
     * @param period: "daily", "weekly", "monthly"
     */
    fun getLeaderboard(type: String, period: String): Flow<Result<List<RankingUser>>> = flow {
        try {
            val response = apiService.getLeaderboard(type, period)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!.users))
            } else {
                emit(Result.failure(Exception("Failed to fetch leaderboard: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get current user's rank for a specific leaderboard type
     */
    fun getMyRank(type: String): Flow<Result<RankingUser>> = flow {
        try {
            val response = apiService.getMyRank(type)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Failed to fetch my rank: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
