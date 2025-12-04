package com.aura.voicechat.config

/**
 * Owner/Admin Configuration
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Hardcoded owner credentials and permissions
 */
object OwnerConfig {
    
    // Owner Credentials
    const val OWNER_EMAIL = "Hamziii886@gmail.com"
    const val OWNER_PASSWORD = "MnIHbK123xD"
    
    // Owner User ID (should match backend)
    const val OWNER_USER_ID = "owner_admin_001"
    
    /**
     * Check if a user is the owner
     */
    fun isOwner(userId: String?): Boolean {
        return userId == OWNER_USER_ID
    }
    
    /**
     * Check if email belongs to owner
     */
    fun isOwnerEmail(email: String?): Boolean {
        return email?.equals(OWNER_EMAIL, ignoreCase = true) == true
    }
    
    /**
     * Owner Permissions
     */
    object Permissions {
        const val CAN_DELETE_ANY_MESSAGE = true
        const val CAN_KICK_ANY_USER = true
        const val CAN_BAN_ANY_USER = true
        const val CAN_MODIFY_ANY_ROOM = true
        const val CAN_VIEW_ALL_REPORTS = true
        const val CAN_MANAGE_EVENTS = true
        const val CAN_MANAGE_MEDALS = true
        const val CAN_ISSUE_DIAMONDS = true
        const val CAN_ISSUE_COINS = true
        const val CAN_ACCESS_ANALYTICS = true
        const val CAN_MODIFY_VIP_TIERS = true
        const val CAN_MANAGE_USERS = true
        const val CAN_VIEW_SENSITIVE_DATA = true
        const val BYPASS_ALL_RESTRICTIONS = true
    }
    
    /**
     * Check if user has owner permissions
     */
    fun hasOwnerPermissions(userId: String?): Boolean {
        return isOwner(userId)
    }
    
    /**
     * Get owner display name
     */
    fun getOwnerDisplayName(): String {
        return "Aura Admin"
    }
    
    /**
     * Get owner badge icon
     */
    fun getOwnerBadge(): String {
        return "👑" // Crown emoji for owner
    }
}
