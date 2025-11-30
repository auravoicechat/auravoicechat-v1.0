package com.aura.voicechat.ui.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * ViewModel for Greedy Baby Game
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class GreedyBabyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GreedyBabyUiState())
    val uiState: StateFlow<GreedyBabyUiState> = _uiState.asStateFlow()

    init {
        loadGameData()
    }

    private fun loadGameData() {
        // Initialize available foods
        val foods = listOf(
            FoodItem("milk", "Milk", "🍼", 100, 10, 1.0f),
            FoodItem("apple", "Apple", "🍎", 150, 15, 1.2f),
            FoodItem("banana", "Banana", "🍌", 150, 15, 1.2f),
            FoodItem("cookie", "Cookie", "🍪", 200, 20, 1.5f),
            FoodItem("cake", "Cake", "🍰", 300, 25, 1.8f),
            FoodItem("candy", "Candy", "🍬", 250, 18, 1.4f),
            FoodItem("ice_cream", "Ice Cream", "🍦", 350, 30, 2.0f),
            FoodItem("chocolate", "Chocolate", "🍫", 400, 35, 2.2f),
            FoodItem("pizza", "Pizza", "🍕", 500, 40, 2.5f),
            FoodItem("burger", "Burger", "🍔", 500, 40, 2.5f),
            FoodItem("sushi", "Sushi", "🍣", 600, 45, 2.8f),
            FoodItem("steak", "Steak", "🥩", 800, 50, 3.0f)
        )

        _uiState.update { state ->
            state.copy(
                availableFoods = foods,
                selectedFood = foods.first(),
                userCoins = 10000, // This would come from user repository
                maxFeeds = 10
            )
        }
    }

    fun selectFood(food: FoodItem) {
        _uiState.update { state ->
            state.copy(selectedFood = food)
        }
    }

    fun feedBaby() {
        val currentState = _uiState.value
        val food = currentState.selectedFood ?: return

        if (currentState.userCoins < food.cost) {
            // Not enough coins
            return
        }

        if (currentState.feedCount >= currentState.maxFeeds) {
            // Max feeds reached
            return
        }

        viewModelScope.launch {
            // Start feeding animation
            _uiState.update { it.copy(isFeeding = true) }

            // Deduct coins
            _uiState.update { state ->
                state.copy(userCoins = state.userCoins - food.cost)
            }

            delay(1500) // Feeding animation time

            // Calculate result
            val isSuccessful = Random.nextFloat() < 0.7f // 70% success rate
            val newHappiness = minOf(100, currentState.happinessLevel + food.happinessBonus)
            
            val reward = if (isSuccessful) {
                val baseReward = (food.cost * food.rewardMultiplier).toInt()
                val bonusMultiplier = 1.0f + (newHappiness / 100f) * 0.5f
                val finalReward = (baseReward * bonusMultiplier).toInt()
                
                GameReward(
                    type = if (Random.nextFloat() < 0.2f) "Diamonds" else "Coins",
                    amount = finalReward
                )
            } else null

            _uiState.update { state ->
                state.copy(
                    isFeeding = false,
                    feedCount = state.feedCount + 1,
                    happinessLevel = newHappiness,
                    isHappy = isSuccessful,
                    isFull = newHappiness >= 100,
                    reward = reward,
                    showResult = true,
                    userCoins = state.userCoins + (reward?.amount ?: 0)
                )
            }
        }
    }

    fun dismissResult() {
        _uiState.update { state ->
            state.copy(
                showResult = false,
                reward = null
            )
        }
    }
}

data class GreedyBabyUiState(
    val isLoading: Boolean = false,
    val userCoins: Int = 0,
    val feedCount: Int = 0,
    val maxFeeds: Int = 10,
    val happinessLevel: Int = 50,
    val isHappy: Boolean = true,
    val isFull: Boolean = false,
    val isFeeding: Boolean = false,
    val availableFoods: List<FoodItem> = emptyList(),
    val selectedFood: FoodItem? = null,
    val reward: GameReward? = null,
    val showResult: Boolean = false,
    val error: String? = null
) {
    val canFeed: Boolean
        get() = selectedFood != null &&
                userCoins >= (selectedFood?.cost ?: 0) &&
                feedCount < maxFeeds &&
                !isFull
}
