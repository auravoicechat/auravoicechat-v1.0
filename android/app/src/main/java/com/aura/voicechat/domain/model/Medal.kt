package com.aura.voicechat.domain.model

/**
 * Medal domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Gift, Achievement, and Activity medals with display customization
 */
data class Medal(
    val id: String,
    val name: String,
    val category: MedalCategory,
    val description: String,
    val icon: String,
    val milestone: Long,
    val milestoneType: String,
    val rewards: MedalReward?,
    val duration: String,
    val earnedAt: Long?,
    val isDisplayed: Boolean
)

enum class MedalCategory {
    GIFT,
    ACHIEVEMENT,
    ACTIVITY,
    SPECIAL
}

data class MedalReward(
    val coins: Long?,
    val cosmetic: CosmeticReward?
)

data class CosmeticReward(
    val type: String,
    val id: String,
    val duration: String
)

data class MedalDisplaySettings(
    val displayedMedals: List<String>,
    val hiddenMedals: List<String>,
    val maxDisplayed: Int = 10
)
