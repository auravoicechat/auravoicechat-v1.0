package com.aura.voicechat.domain.repository

import com.aura.voicechat.data.local.entity.MedalEntity
import com.aura.voicechat.data.model.AllMedalsResponse
import com.aura.voicechat.data.model.MedalDetailDto
import kotlinx.coroutines.flow.Flow

/**
 * Medal Repository Interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface MedalRepository {
    suspend fun getAllMedals(forceRefresh: Boolean = false): Result<AllMedalsResponse>
    suspend fun getMyMedals(forceRefresh: Boolean = false): Result<AllMedalsResponse>
    suspend fun getMedalDetails(medalId: String): Result<MedalDetailDto>
    suspend fun equipMedal(medalId: String): Result<Unit>
    suspend fun unequipMedal(medalId: String): Result<Unit>
    fun getCachedMedals(): Flow<List<MedalEntity>>
    fun getCachedMedalsByCategory(category: String): Flow<List<MedalEntity>>
}
