package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.ExchangeRequest
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.model.*
import com.aura.voicechat.domain.repository.WalletRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wallet Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WalletRepository {
    
    override suspend fun getWallet(): Result<Wallet> {
        return try {
            val response = apiService.getWalletBalances()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val wallet = Wallet(
                    coins = dto.coins,
                    diamonds = dto.diamonds,
                    lastUpdated = System.currentTimeMillis()
                )
                Result.success(wallet)
            } else {
                Result.failure(Exception("Failed to load wallet"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun exchangeDiamondsToCoins(diamonds: Long): Result<ExchangeResult> {
        return try {
            val response = apiService.exchangeDiamondsToCoins(ExchangeRequest(diamonds))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val result = ExchangeResult(
                    success = dto.success,
                    diamondsUsed = dto.diamondsUsed,
                    coinsReceived = dto.coinsReceived,
                    newBalance = Wallet(
                        coins = dto.newBalance.coins,
                        diamonds = dto.newBalance.diamonds,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                Result.success(result)
            } else {
                Result.failure(Exception("Exchange failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTransactionHistory(page: Int, pageSize: Int): Result<List<Transaction>> {
        return Result.success(emptyList())
    }
    
    override suspend fun transferCoins(toUserId: String, amount: Long): Result<Unit> {
        return Result.success(Unit)
    }
}
