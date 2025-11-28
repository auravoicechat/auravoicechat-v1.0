package com.aura.voicechat.domain.model

/**
 * User domain model
 * Developer: Hawkaye Visions LTD — Pakistan
 */
data class User(
    val id: String,
    val name: String,
    val avatar: String?,
    val level: Int,
    val exp: Long,
    val vipTier: Int,
    val vipExpiry: Long?,
    val coins: Long,
    val diamonds: Long,
    val gender: Gender,
    val country: String?,
    val bio: String?,
    val isOnline: Boolean,
    val lastActiveAt: Long?,
    val kycStatus: KycStatus,
    val cpPartnerId: String?,
    val familyId: String?,
    val createdAt: Long
)

enum class Gender {
    MALE, FEMALE, OTHER, UNSPECIFIED
}

enum class KycStatus {
    NOT_STARTED,
    ID_SUBMITTED,
    SELFIE_SUBMITTED,
    PENDING_REVIEW,
    VERIFIED,
    REJECTED
}
