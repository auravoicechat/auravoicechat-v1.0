package com.aura.voicechat.domain.repository

import com.aura.voicechat.domain.model.User

/**
 * User Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface UserRepository {
    suspend fun getCurrentUser(): Result<User>
    suspend fun getUser(userId: String): Result<User>
    suspend fun updateProfile(name: String?, bio: String?, avatar: String?): Result<Unit>
    suspend fun followUser(userId: String): Result<Unit>
    suspend fun unfollowUser(userId: String): Result<Unit>
    suspend fun getFollowers(userId: String): Result<List<User>>
    suspend fun getFollowing(userId: String): Result<List<User>>
}
