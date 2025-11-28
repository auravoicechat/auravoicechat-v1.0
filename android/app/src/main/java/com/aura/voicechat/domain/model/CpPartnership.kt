package com.aura.voicechat.domain.model

/**
 * CP (Couple Partnership) domain models
 * Developer: Hawkaye Visions LTD — Pakistan
 */
data class CpPartnership(
    val id: String,
    val partner1Id: String,
    val partner1Name: String,
    val partner1Avatar: String?,
    val partner2Id: String,
    val partner2Name: String,
    val partner2Avatar: String?,
    val level: Int,
    val exp: Long,
    val expToNextLevel: Long,
    val createdAt: Long
)

data class CpLevel(
    val level: Int,
    val name: String,
    val expRequired: Long,
    val rewards: List<CpReward>
)

data class CpReward(
    val type: String,
    val id: String?,
    val amount: Long?,
    val duration: String?
)

/**
 * CP Level progression as per cp-levels.json
 */
object CpLevels {
    val levels = listOf(
        CpLevel(1, "New Couple", 0, listOf(CpReward("frame", "cp_frame_1", null, "7d"))),
        CpLevel(2, "Sweet", 1000, listOf(CpReward("coins", null, 10_000, null))),
        CpLevel(3, "Loving", 5000, listOf(CpReward("frame", "cp_frame_3", null, "14d"))),
        CpLevel(4, "Devoted", 15000, listOf(CpReward("coins", null, 50_000, null))),
        CpLevel(5, "Growing Love", 35000, listOf(CpReward("frame", "cp_frame_5", null, "30d"))),
        CpLevel(6, "Deep Bond", 70000, listOf(CpReward("coins", null, 100_000, null))),
        CpLevel(7, "Inseparable", 120000, listOf(CpReward("vehicle", "cp_vehicle_7", null, "30d"))),
        CpLevel(8, "Soulmates", 200000, listOf(CpReward("coins", null, 250_000, null))),
        CpLevel(9, "Destined", 350000, listOf(CpReward("frame", "cp_frame_9", null, "permanent"))),
        CpLevel(10, "Eternal Love", 600000, listOf(CpReward("legendary_set", "cp_legendary_set", null, "permanent")))
    )
}
