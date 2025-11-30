package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.*
import com.aura.voicechat.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Store Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * Get store catalog
     */
    suspend fun getStoreCatalog(category: String? = null): Result<StoreCatalogResponse> {
        return try {
            val response = apiService.getStoreCatalog(category)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch store catalog"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get featured items
     */
    suspend fun getFeaturedItems(): Result<StoreCatalogResponse> {
        return try {
            val response = apiService.getFeaturedItems()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch featured items"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Purchase an item
     */
    suspend fun purchaseItem(itemId: String, quantity: Int = 1): Result<PurchaseItemResponse> {
        return try {
            val request = PurchaseItemRequest(itemId, quantity)
            val response = apiService.purchaseItem(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to purchase item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get single store item details
     */
    suspend fun getStoreItem(itemId: String): Result<StoreItemDto> {
        return try {
            val response = apiService.getStoreItem(itemId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch store item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
