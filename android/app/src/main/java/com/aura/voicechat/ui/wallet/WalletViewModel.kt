package com.aura.voicechat.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.model.Currency
import com.aura.voicechat.domain.model.Transaction
import com.aura.voicechat.domain.model.TransactionType
import com.aura.voicechat.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Wallet ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()
    
    init {
        loadWallet()
        loadTransactions()
    }
    
    private fun loadWallet() {
        viewModelScope.launch {
            try {
                // Mock wallet data
                _uiState.value = _uiState.value.copy(
                    coins = 1_500_000,
                    diamonds = 250_000
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                // Mock transactions
                val transactions = listOf(
                    Transaction(
                        id = "tx_1",
                        type = TransactionType.DAILY_REWARD,
                        amount = 30_000,
                        currency = Currency.COINS,
                        description = "Daily Login Reward (Day 6)",
                        timestamp = System.currentTimeMillis() - 3600_000,
                        relatedUserId = null,
                        relatedUserName = null
                    ),
                    Transaction(
                        id = "tx_2",
                        type = TransactionType.GIFT_SENT,
                        amount = 50_000,
                        currency = Currency.COINS,
                        description = "Sent Rose to Sarah",
                        timestamp = System.currentTimeMillis() - 7200_000,
                        relatedUserId = "user_sarah",
                        relatedUserName = "Sarah"
                    ),
                    Transaction(
                        id = "tx_3",
                        type = TransactionType.GIFT_RECEIVED,
                        amount = 100_000,
                        currency = Currency.DIAMONDS,
                        description = "Received Crown from Mike",
                        timestamp = System.currentTimeMillis() - 86400_000,
                        relatedUserId = "user_mike",
                        relatedUserName = "Mike"
                    ),
                    Transaction(
                        id = "tx_4",
                        type = TransactionType.EXCHANGE,
                        amount = 30_000,
                        currency = Currency.COINS,
                        description = "Exchanged 100K Diamonds",
                        timestamp = System.currentTimeMillis() - 172800_000,
                        relatedUserId = null,
                        relatedUserName = null
                    )
                )
                
                _uiState.value = _uiState.value.copy(transactions = transactions)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun exchangeDiamondsToCoins(diamonds: Long) {
        viewModelScope.launch {
            try {
                val coinsReceived = (diamonds * 0.30).toLong()
                
                _uiState.value = _uiState.value.copy(
                    diamonds = _uiState.value.diamonds - diamonds,
                    coins = _uiState.value.coins + coinsReceived,
                    message = "Successfully exchanged $diamonds diamonds for $coinsReceived coins!"
                )
                
                // Add transaction to history
                val newTransaction = Transaction(
                    id = "tx_${System.currentTimeMillis()}",
                    type = TransactionType.EXCHANGE,
                    amount = coinsReceived,
                    currency = Currency.COINS,
                    description = "Exchanged ${diamonds / 1000}K Diamonds",
                    timestamp = System.currentTimeMillis(),
                    relatedUserId = null,
                    relatedUserName = null
                )
                
                _uiState.value = _uiState.value.copy(
                    transactions = listOf(newTransaction) + _uiState.value.transactions
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class WalletUiState(
    val isLoading: Boolean = false,
    val coins: Long = 0,
    val diamonds: Long = 0,
    val transactions: List<Transaction> = emptyList(),
    val message: String? = null,
    val error: String? = null
)
