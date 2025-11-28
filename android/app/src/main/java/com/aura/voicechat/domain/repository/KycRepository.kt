package com.aura.voicechat.domain.repository

import com.aura.voicechat.domain.model.KycData
import com.aura.voicechat.domain.model.KycSubmission
import com.aura.voicechat.domain.model.LivenessResult

/**
 * KYC Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Only ID Card (front/back) + Selfie - NO utility bills
 */
interface KycRepository {
    suspend fun getKycStatus(): Result<KycData>
    suspend fun submitKyc(submission: KycSubmission): Result<Unit>
    suspend fun uploadIdCardFront(imageUri: String): Result<String>
    suspend fun uploadIdCardBack(imageUri: String): Result<String>
    suspend fun uploadSelfie(imageUri: String): Result<String>
    suspend fun performLivenessCheck(): Result<LivenessResult>
}
