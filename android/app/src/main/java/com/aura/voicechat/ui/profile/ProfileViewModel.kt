package com.aura.voicechat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.domain.model.*
import com.aura.voicechat.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Profile ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Mock user data
                val user = User(
                    id = if (userId == "me") "user_current" else userId,
                    name = if (userId == "me") "John Doe" else "User $userId",
                    avatar = null,
                    level = 25,
                    exp = 125_000,
                    vipTier = 5,
                    vipExpiry = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L,
                    coins = 1_500_000,
                    diamonds = 250_000,
                    gender = Gender.MALE,
                    country = "US",
                    bio = "Welcome to my profile! 🎤",
                    isOnline = true,
                    lastActiveAt = System.currentTimeMillis(),
                    kycStatus = KycStatus.VERIFIED,
                    cpPartnerId = null,
                    familyId = null,
                    createdAt = System.currentTimeMillis() - 90 * 24 * 60 * 60 * 1000L
                )
                
                val medals = listOf(
                    Medal(
                        id = "gift_sender_3",
                        name = "Gift Sender III",
                        category = MedalCategory.GIFT,
                        description = "Send 1M coins in gifts",
                        icon = "medal_gift_sender_3.png",
                        milestone = 1_000_000,
                        milestoneType = "coins_sent",
                        rewards = MedalReward(coins = 25_000, cosmetic = null),
                        duration = "permanent",
                        earnedAt = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L,
                        isDisplayed = true
                    ),
                    Medal(
                        id = "login_30d",
                        name = "30 Day Veteran",
                        category = MedalCategory.ACTIVITY,
                        description = "Log in for 30 cumulative days",
                        icon = "medal_login_30d.png",
                        milestone = 30,
                        milestoneType = "login_days",
                        rewards = MedalReward(coins = 50_000, cosmetic = CosmeticReward("frame", "frame_30d", "7d")),
                        duration = "permanent",
                        earnedAt = System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000L,
                        isDisplayed = true
                    ),
                    Medal(
                        id = "level_20",
                        name = "Established",
                        category = MedalCategory.ACHIEVEMENT,
                        description = "Reach Level 20",
                        icon = "medal_level_20.png",
                        milestone = 20,
                        milestoneType = "user_level",
                        rewards = MedalReward(coins = 25_000, cosmetic = CosmeticReward("frame", "frame_level_20", "14d")),
                        duration = "permanent",
                        earnedAt = System.currentTimeMillis() - 21 * 24 * 60 * 60 * 1000L,
                        isDisplayed = true
                    )
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    medals = medals,
                    followersCount = 1234,
                    followingCount = 567,
                    giftsReceivedCount = 890,
                    isFollowing = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun toggleFollow() {
        viewModelScope.launch {
            val currentState = _uiState.value.isFollowing
            _uiState.value = _uiState.value.copy(
                isFollowing = !currentState,
                followersCount = if (currentState) 
                    _uiState.value.followersCount - 1 
                else 
                    _uiState.value.followersCount + 1
            )
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val medals: List<Medal> = emptyList(),
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val giftsReceivedCount: Int = 0,
    val isFollowing: Boolean = false,
    val error: String? = null
)
