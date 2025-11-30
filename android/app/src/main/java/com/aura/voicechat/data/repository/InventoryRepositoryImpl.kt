package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.*
import com.aura.voicechat.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Inventory Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * Get user's inventory
     */
    suspend fun getInventory(category: String? = null): Result<InventoryResponse> {
        return try {
            val response = apiService.getInventory(category)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch inventory"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get currently equipped items
     */
    suspend fun getEquippedItems(): Result<EquippedItemsResponse> {
        return try {
            val response = apiService.getEquippedItems()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch equipped items"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Equip an item
     */
    suspend fun equipItem(itemId: String): Result<Unit> {
        return try {
            val request = EquipItemRequest(itemId)
            val response = apiService.equipItem(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to equip item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Unequip an item
     */
    suspend fun unequipItem(category: String): Result<Unit> {
        return try {
            val request = UnequipItemRequest(category)
            val response = apiService.unequipItem(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to unequip item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get baggage (free gifts to send)
     */
    suspend fun getBaggage(): Result<BaggageResponse> {
        return try {
            val response = apiService.getBaggage()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch baggage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
