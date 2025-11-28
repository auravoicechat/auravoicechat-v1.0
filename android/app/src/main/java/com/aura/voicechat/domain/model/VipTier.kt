package com.aura.voicechat.domain.model

/**
 * VIP Tier domain model
 * Developer: Hawkaye Visions LTD — Pakistan
 */
data class VipTier(
    val tier: Int,
    val name: String,
    val rechargeRequired: Int,
    val expMultiplier: Double,
    val dailyRewardMultiplier: Double,
    val giftBonusPercent: Int,
    val features: List<String>,
    val monthlyCoins: Long
)

/**
 * VIP tiers with multipliers as per documentation
 * VIP1-VIP10 with multipliers 1.2x-3.0x
 */
object VipTiers {
    val tiers = listOf(
        VipTier(1, "VIP 1", 10, 1.1, 1.2, 5, listOf("vip_badge", "priority_support"), 10_000),
        VipTier(2, "VIP 2", 50, 1.15, 1.4, 7, listOf("vip_badge", "priority_support", "exclusive_frame_7d"), 25_000),
        VipTier(3, "VIP 3", 100, 1.2, 1.6, 10, listOf("vip_badge", "priority_support", "exclusive_frame_7d", "custom_id_discount"), 50_000),
        VipTier(4, "VIP 4", 250, 1.25, 1.8, 12, listOf("vip_badge", "priority_support", "exclusive_frame_14d", "custom_id_discount", "vip_vehicle_7d"), 100_000),
        VipTier(5, "VIP 5", 500, 1.3, 2.0, 15, listOf("vip_badge", "priority_support", "exclusive_frame_30d", "custom_id_discount", "vip_vehicle_14d", "super_mic_access"), 200_000),
        VipTier(6, "VIP 6", 1000, 1.35, 2.2, 18, listOf("vip_badge", "priority_support", "exclusive_frame_30d", "custom_id_free", "vip_vehicle_30d", "super_mic_access", "vip_theme_14d"), 350_000),
        VipTier(7, "VIP 7", 2500, 1.4, 2.4, 20, listOf("vip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "vip_vehicle_30d", "super_mic_access", "vip_theme_30d", "exclusive_games"), 500_000),
        VipTier(8, "VIP 8", 5000, 1.45, 2.6, 22, listOf("vip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "vip_vehicle_permanent", "super_mic_access", "vip_theme_30d", "exclusive_games", "vip_seat_effect"), 750_000),
        VipTier(9, "VIP 9", 10000, 1.5, 2.8, 25, listOf("svip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "vip_vehicle_permanent", "super_mic_access", "vip_theme_permanent", "exclusive_games", "vip_seat_effect", "personal_manager"), 1_000_000),
        VipTier(10, "VIP 10 (SVIP)", 25000, 1.6, 3.0, 30, listOf("svip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "svip_vehicle_permanent", "super_mic_access", "svip_theme_permanent", "exclusive_games", "svip_seat_effect", "personal_manager", "legendary_set"), 2_000_000)
    )
    
    fun getMultiplier(tier: Int): Double {
        return tiers.find { it.tier == tier }?.dailyRewardMultiplier ?: 1.0
    }
}
