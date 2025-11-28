package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.SendOtpRequest
import com.aura.voicechat.data.model.VerifyOtpRequest
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Auth Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    
    override suspend fun sendOtp(phoneNumber: String): Result<Unit> {
        return try {
            val response = apiService.sendOtp(SendOtpRequest(phoneNumber))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to send OTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<Unit> {
        return try {
            val response = apiService.verifyOtp(VerifyOtpRequest(phoneNumber, otp))
            if (response.isSuccessful && response.body()?.success == true) {
                // Store token and user data
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid OTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signInWithGoogle(): Result<Unit> {
        // Implementation with Firebase Auth
        return Result.success(Unit)
    }
    
    override suspend fun signInWithFacebook(): Result<Unit> {
        // Implementation with Firebase Auth
        return Result.success(Unit)
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
