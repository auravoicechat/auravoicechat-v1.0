package com.aura.voicechat.navigation

import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Deep Link Handler
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles both app links (https://auravoice.chat/...) and custom schemes (auravoicechat://...)
 */
@ActivityScoped
class DeepLinkHandler @Inject constructor() {
    
    companion object {
        private const val SCHEME_AURA = "auravoicechat"
        private const val HOST_WEB = "auravoice.chat"
        
        // Deep link paths
        private const val PATH_ROOM = "room"
        private const val PATH_PROFILE = "profile"
        private const val PATH_INVITE = "invite"
        private const val PATH_LIVE = "live"
        private const val PATH_EVENT = "event"
    }
    
    /**
     * Handle deep link from intent
     */
    fun handleDeepLink(intent: Intent?, navController: NavController): Boolean {
        val uri = intent?.data ?: return false
        return handleUri(uri, navController)
    }
    
    /**
     * Handle deep link URI
     */
    fun handleUri(uri: Uri, navController: NavController): Boolean {
        // Check scheme
        val isAuraScheme = uri.scheme == SCHEME_AURA
        val isWebScheme = uri.scheme == "https" && uri.host == HOST_WEB
        
        if (!isAuraScheme && !isWebScheme) {
            return false
        }
        
        // Parse path
        val pathSegments = uri.pathSegments
        if (pathSegments.isEmpty()) {
            return false
        }
        
        return when (pathSegments[0]) {
            PATH_ROOM -> {
                val roomId = pathSegments.getOrNull(1) ?: return false
                navController.navigate("room/$roomId")
                true
            }
            PATH_PROFILE -> {
                val userId = pathSegments.getOrNull(1) ?: return false
                navController.navigate("profile/$userId")
                true
            }
            PATH_LIVE -> {
                val streamId = pathSegments.getOrNull(1)
                if (streamId != null) {
                    navController.navigate("live/$streamId")
                } else {
                    navController.navigate("live/list")
                }
                true
            }
            PATH_EVENT -> {
                val eventId = pathSegments.getOrNull(1) ?: return false
                navController.navigate("event/$eventId")
                true
            }
            PATH_INVITE -> {
                val inviteCode = uri.getQueryParameter("code") ?: return false
                handleInviteCode(inviteCode, navController)
                true
            }
            else -> false
        }
    }
    
    /**
     * Handle invite code
     */
    private fun handleInviteCode(code: String, navController: NavController) {
        // Navigate to referral screen with code
        navController.navigate("referral?code=$code")
    }
    
    /**
     * Generate deep link for room
     */
    fun generateRoomLink(roomId: String): String {
        return "https://$HOST_WEB/$PATH_ROOM/$roomId"
    }
    
    /**
     * Generate deep link for profile
     */
    fun generateProfileLink(userId: String): String {
        return "https://$HOST_WEB/$PATH_PROFILE/$userId"
    }
    
    /**
     * Generate deep link for live stream
     */
    fun generateLiveStreamLink(streamId: String): String {
        return "https://$HOST_WEB/$PATH_LIVE/$streamId"
    }
    
    /**
     * Generate invite link with referral code
     */
    fun generateInviteLink(referralCode: String): String {
        return "https://$HOST_WEB/$PATH_INVITE?code=$referralCode"
    }
}
