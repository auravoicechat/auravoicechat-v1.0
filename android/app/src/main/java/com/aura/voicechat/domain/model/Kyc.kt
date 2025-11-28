package com.aura.voicechat.domain.model

/**
 * KYC (Know Your Customer) domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * IMPORTANT: Only ID Card (front/back) and Selfie verification
 * NO utility bills required
 */
data class KycData(
    val userId: String,
    val status: KycStatus,
    val idCardFront: String?,
    val idCardBack: String?,
    val selfie: String?,
    val livenessScore: Float?,
    val submittedAt: Long?,
    val reviewedAt: Long?,
    val rejectionReason: String?
)

data class KycSubmission(
    val idCardFrontUri: String,
    val idCardBackUri: String,
    val selfieUri: String,
    val livenessCheckPassed: Boolean
)

/**
 * KYC verification steps (NO utility bills)
 */
enum class KycStep {
    ID_CARD_FRONT,
    ID_CARD_BACK,
    SELFIE_CAPTURE,
    LIVENESS_CHECK,
    REVIEW_SUBMIT,
    COMPLETE
}

/**
 * Liveness detection result for selfie verification
 */
data class LivenessResult(
    val passed: Boolean,
    val faceDetected: Boolean,
    val eyesOpen: Boolean,
    val lookingStraight: Boolean,
    val score: Float
)
