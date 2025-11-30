package com.aura.voicechat.ui.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Family System ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class FamilyViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(FamilyUiState())
    val uiState: StateFlow<FamilyUiState> = _uiState.asStateFlow()
    
    init {
        loadFamilyData()
    }
    
    private fun loadFamilyData() {
        viewModelScope.launch {
            // Sample data - would come from API
            _uiState.value = FamilyUiState(
                hasFamily = true,
                familyId = "family123",
                familyName = "Dragon Warriors",
                familyBadge = null,
                familyLevel = 8,
                membersCount = 35,
                maxMembers = 50,
                weeklyGifts = 5400000,
                totalGifts = 145000000,
                weeklyRanking = 12,
                createdDate = "Nov 15, 2024",
                ownerName = "DragonKing",
                familyNotice = "Welcome to Dragon Warriors! Gift daily and help us reach the top!",
                isOwner = false,
                isAdmin = true,
                members = listOf(
                    FamilyMember("user1", "DragonKing", null, "owner", 2500000),
                    FamilyMember("user2", "FireQueen", null, "admin", 1800000),
                    FamilyMember("user3", "IceWarrior", null, "admin", 1200000),
                    FamilyMember("user4", "StormBringer", null, "member", 800000),
                    FamilyMember("user5", "ThunderBolt", null, "member", 650000),
                    FamilyMember("user6", "ShadowNinja", null, "member", 500000),
                    FamilyMember("user7", "LightMage", null, "member", 450000)
                ),
                recentActivities = listOf(
                    FamilyActivity("1", "gift", "FireQueen sent 100K diamonds", "2 min ago"),
                    FamilyActivity("2", "join", "NewMember joined the family", "1 hour ago"),
                    FamilyActivity("3", "gift", "DragonKing sent 500K diamonds", "3 hours ago"),
                    FamilyActivity("4", "levelup", "Family reached Level 8!", "1 day ago")
                ),
                perks = listOf(
                    FamilyPerk("1", "Family Badge", "Display family badge on profile", 1),
                    FamilyPerk("2", "Family Chat", "Private family chat room", 2),
                    FamilyPerk("3", "Gift Bonus 5%", "5% extra diamonds from gifts", 3),
                    FamilyPerk("4", "Family Room", "Exclusive family room access", 5),
                    FamilyPerk("5", "Gift Bonus 10%", "10% extra diamonds from gifts", 7),
                    FamilyPerk("6", "Family Theme", "Unlock family-exclusive theme", 10),
                    FamilyPerk("7", "Gift Bonus 15%", "15% extra diamonds from gifts", 15),
                    FamilyPerk("8", "Family Vehicle", "Exclusive family vehicle", 20)
                )
            )
        }
    }
    
    fun showCreateFamilyDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = true)
    }
    
    fun dismissCreateFamilyDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = false)
    }
    
    fun showJoinFamilyDialog() {
        _uiState.value = _uiState.value.copy(showJoinDialog = true)
    }
    
    fun dismissJoinFamilyDialog() {
        _uiState.value = _uiState.value.copy(showJoinDialog = false)
    }
    
    fun showSettingsDialog() {
        _uiState.value = _uiState.value.copy(showSettingsDialog = true)
    }
    
    fun createFamily(name: String, badge: String?) {
        viewModelScope.launch {
            // Call API to create family
            dismissCreateFamilyDialog()
        }
    }
    
    fun joinFamily(familyId: String) {
        viewModelScope.launch {
            // Call API to join family
            dismissJoinFamilyDialog()
        }
    }
    
    fun kickMember(userId: String) {
        viewModelScope.launch {
            // Call API to kick member
        }
    }
    
    fun promoteMember(userId: String) {
        viewModelScope.launch {
            // Call API to promote member
        }
    }
}

data class FamilyUiState(
    val isLoading: Boolean = false,
    val hasFamily: Boolean = false,
    val familyId: String = "",
    val familyName: String = "",
    val familyBadge: String? = null,
    val familyLevel: Int = 0,
    val membersCount: Int = 0,
    val maxMembers: Int = 50,
    val weeklyGifts: Long = 0,
    val totalGifts: Long = 0,
    val weeklyRanking: Int = 0,
    val createdDate: String = "",
    val ownerName: String = "",
    val familyNotice: String = "",
    val isOwner: Boolean = false,
    val isAdmin: Boolean = false,
    val members: List<FamilyMember> = emptyList(),
    val recentActivities: List<FamilyActivity> = emptyList(),
    val perks: List<FamilyPerk> = emptyList(),
    val showCreateDialog: Boolean = false,
    val showJoinDialog: Boolean = false,
    val showSettingsDialog: Boolean = false
)
