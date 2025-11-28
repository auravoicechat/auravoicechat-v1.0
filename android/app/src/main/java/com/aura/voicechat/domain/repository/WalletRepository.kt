package com.aura.voicechat.domain.repository

import com.aura.voicechat.domain.model.ExchangeResult
import com.aura.voicechat.domain.model.Transaction
import com.aura.voicechat.domain.model.Wallet

/**
 * Wallet Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface WalletRepository {
    suspend fun getWallet(): Result<Wallet>
    suspend fun exchangeDiamondsToCoins(diamonds: Long): Result<ExchangeResult>
    suspend fun getTransactionHistory(page: Int, pageSize: Int): Result<List<Transaction>>
    suspend fun transferCoins(toUserId: String, amount: Long): Result<Unit>
}
