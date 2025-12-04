package com.aura.voicechat.data.repository

import com.aura.voicechat.data.local.dao.MedalDao
import com.aura.voicechat.data.local.entity.MedalEntity
import com.aura.voicechat.data.model.AllMedalsResponse
import com.aura.voicechat.data.model.MedalDetailDto
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.MedalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Medal operations
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class MedalRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val medalDao: MedalDao
) : MedalRepository {
    
    override suspend fun getAllMedals(forceRefresh: Boolean): Result<AllMedalsResponse> {
        return try {
            if (!forceRefresh) {
                // Check cache first
                // If cache is recent enough, return it
            }
            
            val response = apiService.getAllMedals()
            
            if (response.isSuccessful && response.body() != null) {
                val medals = response.body()!!
                
                // Cache the medals
                val entities = medals.medals.map { dto ->
                    MedalEntity(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        iconUrl = dto.iconUrl,
                        category = dto.category,
                        rarity = dto.rarity,
                        isUnlocked = dto.isUnlocked,
                        progress = dto.progress,
                        progressTarget = dto.progressTarget,
                        unlockedAt = dto.unlockedAt,
                        howToEarn = dto.howToEarn
                    )
                }
                medalDao.insertMedals(entities)
                
                Result.success(medals)
            } else {
                Result.failure(Exception("Failed to get medals: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMyMedals(forceRefresh: Boolean): Result<AllMedalsResponse> {
        return try {
            val response = apiService.getMyMedals()
            
            if (response.isSuccessful && response.body() != null) {
                val medals = response.body()!!
                
                // Cache the medals
                val entities = medals.medals.map { dto ->
                    MedalEntity(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        iconUrl = dto.iconUrl,
                        category = dto.category,
                        rarity = dto.rarity,
                        isUnlocked = dto.isUnlocked,
                        progress = dto.progress,
                        progressTarget = dto.progressTarget,
                        unlockedAt = dto.unlockedAt,
                        howToEarn = dto.howToEarn
                    )
                }
                medalDao.insertMedals(entities)
                
                Result.success(medals)
            } else {
                Result.failure(Exception("Failed to get my medals: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMedalDetails(medalId: String): Result<MedalDetailDto> {
        return try {
            val response = apiService.getMedalDetails(medalId)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get medal details: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun equipMedal(medalId: String): Result<Unit> {
        return try {
            val response = apiService.equipMedal(medalId)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to equip medal: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun unequipMedal(medalId: String): Result<Unit> {
        return try {
            val response = apiService.unequipMedal(medalId)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to unequip medal: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getCachedMedals(): Flow<List<MedalEntity>> {
        return medalDao.getAllMedals()
    }
    
    override fun getCachedMedalsByCategory(category: String): Flow<List<MedalEntity>> {
        return medalDao.getMedalsByCategory(category)
    }
}
