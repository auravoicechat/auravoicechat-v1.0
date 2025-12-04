package com.aura.voicechat.data.repository

import com.aura.voicechat.data.local.dao.FaqDao
import com.aura.voicechat.data.local.entity.FaqEntity
import com.aura.voicechat.data.model.*
import com.aura.voicechat.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Settings operations
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface SettingsRepository {
    suspend fun getFaqs(category: String? = null): Result<FaqResponse>
    suspend fun submitFeedback(
        type: String,
        subject: String,
        description: String,
        screenshots: List<String>?
    ): Result<SubmitFeedbackResponse>
    suspend fun checkUpdate(): Result<AppVersionResponse>
    suspend fun getCacheSize(): Result<CacheSizeResponse>
    suspend fun clearCache(): Result<Unit>
    fun getCachedFaqs(): Flow<List<FaqEntity>>
}

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val faqDao: FaqDao
) : SettingsRepository {
    
    override suspend fun getFaqs(category: String?): Result<FaqResponse> {
        return try {
            val response = apiService.getFaqs(category)
            
            if (response.isSuccessful && response.body() != null) {
                val faqs = response.body()!!
                
                // Cache FAQs
                val entities = faqs.faqs.map { dto ->
                    FaqEntity(
                        id = dto.id,
                        question = dto.question,
                        answer = dto.answer,
                        category = dto.category,
                        order = dto.order
                    )
                }
                faqDao.insertFaqs(entities)
                
                Result.success(faqs)
            } else {
                Result.failure(Exception("Failed to get FAQs: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun submitFeedback(
        type: String,
        subject: String,
        description: String,
        screenshots: List<String>?
    ): Result<SubmitFeedbackResponse> {
        return try {
            val request = SubmitFeedbackRequest(
                type = type,
                subject = subject,
                description = description,
                screenshots = screenshots
            )
            
            val response = apiService.submitFeedback(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to submit feedback: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun checkUpdate(): Result<AppVersionResponse> {
        return try {
            val response = apiService.checkUpdate()
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to check for updates: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCacheSize(): Result<CacheSizeResponse> {
        return try {
            val response = apiService.getCacheSize()
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get cache size: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun clearCache(): Result<Unit> {
        return try {
            val response = apiService.clearCache()
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to clear cache: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getCachedFaqs(): Flow<List<FaqEntity>> {
        return faqDao.getAllFaqs()
    }
}
