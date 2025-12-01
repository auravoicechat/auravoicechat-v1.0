package com.aura.voicechat.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Auth Interceptor for OkHttp
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Intercepts API requests and adds the Authorization header with the JWT token
 * for authenticated endpoints. Public endpoints (like health, auth) are not modified.
 */
class AuthInterceptor(
    private val context: Context
) : Interceptor {
    
    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
        
        // Endpoints that don't require authentication
        private val PUBLIC_ENDPOINTS = listOf(
            "/health",
            "/api/v1/auth/otp/send",
            "/api/v1/auth/otp/verify",
            "/api/v1/auth/google",
            "/api/v1/auth/facebook",
            "/api/v1/auth/refresh"
        )
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestPath = originalRequest.url.encodedPath
        
        // Skip authentication for public endpoints
        if (isPublicEndpoint(requestPath)) {
            return chain.proceed(originalRequest)
        }
        
        // Get the auth token from SharedPreferences
        val token = getAuthToken()
        
        // If no token available, proceed without auth header
        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }
        
        // Add the Authorization header
        val authenticatedRequest = originalRequest.newBuilder()
            .header(HEADER_AUTHORIZATION, "$BEARER_PREFIX$token")
            .build()
        
        return chain.proceed(authenticatedRequest)
    }
    
    /**
     * Check if the endpoint is public (doesn't require authentication).
     */
    private fun isPublicEndpoint(path: String): Boolean {
        return PUBLIC_ENDPOINTS.any { path.endsWith(it) || path.contains(it) }
    }
    
    /**
     * Get the auth token from SharedPreferences.
     */
    private fun getAuthToken(): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }
}
