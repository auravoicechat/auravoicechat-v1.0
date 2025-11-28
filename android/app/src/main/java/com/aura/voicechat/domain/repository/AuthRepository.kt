package com.aura.voicechat.domain.repository

/**
 * Authentication Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Result<Unit>
    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<Unit>
    suspend fun signInWithGoogle(): Result<Unit>
    suspend fun signInWithFacebook(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUserId(): String?
    suspend fun isLoggedIn(): Boolean
}
