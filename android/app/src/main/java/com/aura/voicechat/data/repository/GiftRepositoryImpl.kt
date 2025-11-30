package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.*
import com.aura.voicechat.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gift Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class GiftRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * Get gift catalog - fetches all gifts from server
     * Gift data is managed by Owner CMS
     */
    suspend fun getGiftCatalog(
        region: String? = null,
        category: String? = null
    ): Result<GiftCatalogResponse> {
        return try {
            val response = apiService.getGiftCatalog(region, category)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch gift catalog"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Send gift to user(s)
     */
    suspend fun sendGift(
        giftId: String,
        recipients: List<String>,
        quantity: Int,
        roomId: String? = null
    ): Result<GiftSendResponseDto> {
        return try {
            val request = GiftSendRequestDto(
                giftId = giftId,
                recipients = recipients,
                quantity = quantity,
                roomId = roomId
            )
            val response = apiService.sendGift(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to send gift"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Send baggage gift (free gift from inventory)
     */
    suspend fun sendBaggageGift(
        baggageItemId: String,
        recipient: String,
        roomId: String? = null
    ): Result<GiftSendResponseDto> {
        return try {
            val request = BaggageSendRequestDto(
                baggageItemId = baggageItemId,
                recipient = recipient,
                roomId = roomId
            )
            val response = apiService.sendBaggageGift(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to send baggage gift"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get gift transaction history
     */
    suspend fun getGiftHistory(
        type: String,
        page: Int,
        limit: Int
    ): Result<GiftHistoryResponse> {
        return try {
            val response = apiService.getGiftHistory(type, page, limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch gift history"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
