package com.aura.voicechat.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Events ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class EventsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()
    
    fun loadEvent(eventType: String) {
        viewModelScope.launch {
            when (eventType) {
                "recharge" -> loadRechargeEvent()
                "party_star" -> loadPartyStarEvent()
                "room_support" -> loadRoomSupportEvent()
            }
        }
    }
    
    private fun loadRechargeEvent() {
        _uiState.value = EventsUiState(
            eventName = "Monthly Recharge Rewards",
            timeRemaining = "5 days 12:34:56",
            rechargeTiers = listOf(
                RechargeTier("t1", 100000, 100000, "Bronze Frame (7d)", 10000, true, true),
                RechargeTier("t2", 500000, 350000, "Silver Frame (14d)", 50000, false, false),
                RechargeTier("t3", 1000000, 350000, "Gold Frame (30d)", 100000, false, false),
                RechargeTier("t4", 5000000, 350000, "Diamond Frame (30d)", 500000, false, false),
                RechargeTier("t5", 10000000, 350000, "Legendary Vehicle", 1000000, false, false)
            )
        )
    }
    
    private fun loadPartyStarEvent() {
        _uiState.value = EventsUiState(
            eventName = "Weekly Party Star",
            timeRemaining = "3 days 08:15:30",
            myPoints = 125000,
            myRank = 45,
            nextTierPoints = 200000,
            topStars = listOf(
                PartyStar("user1", "DiamondQueen", 58, 1, 8500000),
                PartyStar("user2", "GoldKing", 55, 2, 7200000),
                PartyStar("user3", "SilverStar", 52, 3, 6100000),
                PartyStar("user4", "RubyPrincess", 48, 4, 5400000),
                PartyStar("user5", "EmeraldKnight", 45, 5, 4800000),
                PartyStar("user6", "SapphireMage", 42, 6, 4200000),
                PartyStar("user7", "PearlDancer", 40, 7, 3600000),
                PartyStar("user8", "OpalSinger", 38, 8, 3100000),
                PartyStar("user9", "AmethystDreamer", 35, 9, 2700000),
                PartyStar("user10", "TopazWarrior", 33, 10, 2300000)
            ),
            partyRewards = listOf(
                PartyReward("#1", "5,000,000 coins + Crown Frame"),
                PartyReward("#2-3", "2,000,000 coins + Gold Frame"),
                PartyReward("#4-10", "500,000 coins + Silver Frame"),
                PartyReward("#11-50", "100,000 coins"),
                PartyReward("#51-100", "50,000 coins")
            )
        )
    }
    
    private fun loadRoomSupportEvent() {
        _uiState.value = EventsUiState(
            eventName = "Support Dragon Warriors Room",
            timeRemaining = "Permanent",
            supportOptions = listOf(
                SupportOption("s1", "Bronze Support", "Show love to the room", 10000),
                SupportOption("s2", "Silver Support", "Boost room visibility", 50000),
                SupportOption("s3", "Gold Support", "Premium room boost", 200000),
                SupportOption("s4", "Diamond Support", "Ultimate room support", 1000000)
            ),
            topSupporters = listOf(
                Supporter("user1", "StarLight", 1, 5000000),
                Supporter("user2", "MoonRider", 2, 3500000),
                Supporter("user3", "DragonFan", 3, 2800000),
                Supporter("user4", "PhoenixLover", 4, 2100000),
                Supporter("user5", "IceSupporter", 5, 1500000)
            )
        )
    }
    
    fun claimRechargeReward(tierId: String) {
        viewModelScope.launch {
            val updatedTiers = _uiState.value.rechargeTiers.map { tier ->
                if (tier.id == tierId && tier.isCompleted) {
                    tier.copy(isClaimed = true)
                } else {
                    tier
                }
            }
            _uiState.value = _uiState.value.copy(rechargeTiers = updatedTiers)
        }
    }
    
    fun supportRoom(optionId: String) {
        viewModelScope.launch {
            // Process room support
        }
    }
}

data class EventsUiState(
    val isLoading: Boolean = false,
    val eventName: String = "",
    val timeRemaining: String = "",
    val rechargeTiers: List<RechargeTier> = emptyList(),
    val myPoints: Long = 0,
    val myRank: Int = 0,
    val nextTierPoints: Long = 0,
    val topStars: List<PartyStar> = emptyList(),
    val partyRewards: List<PartyReward> = emptyList(),
    val supportOptions: List<SupportOption> = emptyList(),
    val topSupporters: List<Supporter> = emptyList()
)
