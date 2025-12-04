package com.aura.voicechat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Database Entities
 * Developer: Hawkaye Visions LTD — Pakistan
 */

@Entity(
    tableName = "users",
    indices = [Index(value = ["userId"], unique = true)]
)
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val avatar: String?,
    val coverImage: String?,
    val bio: String?,
    val level: Int,
    val vipTier: Int,
    val gender: String, // MALE, FEMALE, UNSPECIFIED
    val birthday: Long?,
    val isOnline: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val visitorsCount: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "conversations",
    indices = [Index(value = ["conversationId"], unique = true)]
)
data class ConversationEntity(
    @PrimaryKey val conversationId: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserAvatar: String?,
    val lastMessage: String?,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val isOnline: Boolean,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "messages",
    indices = [
        Index(value = ["messageId"], unique = true),
        Index(value = ["conversationId"])
    ]
)
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val messageType: String, // TEXT, IMAGE, VOICE, GIFT
    val timestamp: Long,
    val isRead: Boolean,
    val isSent: Boolean,
    val duration: String?, // For voice messages
    val giftName: String?, // For gift messages
    val giftValue: Long?, // For gift messages
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "friends",
    indices = [
        Index(value = ["userId"], unique = true),
        Index(value = ["isOnline"])
    ]
)
data class FriendEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val avatar: String?,
    val level: Int,
    val vipTier: Int,
    val isOnline: Boolean,
    val lastOnline: Long?,
    val friendsSince: Long,
    val mutualFriends: Int = 0,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "friend_requests",
    indices = [Index(value = ["fromUserId"])]
)
data class FriendRequestEntity(
    @PrimaryKey val requestId: String,
    val fromUserId: String,
    val fromUserName: String,
    val fromUserAvatar: String?,
    val fromUserLevel: Int,
    val message: String?,
    val status: String, // PENDING, ACCEPTED, REJECTED
    val createdAt: Long
)

@Entity(tableName = "gifts")
data class GiftEntity(
    @PrimaryKey val giftId: String,
    val name: String,
    val icon: String,
    val animationAsset: String?,
    val category: String,
    val price: Long,
    val rarity: String, // COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
    val animationType: String, // FULLSCREEN, BOTTOM, TOP, FLOAT
    val format: String, // SVGA, LOTTIE, STATIC
    val isAvailable: Boolean,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "families",
    indices = [Index(value = ["familyId"], unique = true)]
)
data class FamilyEntity(
    @PrimaryKey val familyId: String,
    val name: String,
    val avatar: String?,
    val bio: String,
    val level: Int,
    val rank: Int?,
    val ownerId: String,
    val ownerName: String,
    val memberCount: Int,
    val maxMembers: Int,
    val weeklyPoints: Long,
    val totalContributions: Long,
    val isVerified: Boolean,
    val createdAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "family_members",
    indices = [
        Index(value = ["familyId"]),
        Index(value = ["userId"])
    ]
)
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val familyId: String,
    val userId: String,
    val userName: String,
    val userAvatar: String?,
    val userLevel: Int,
    val role: String, // OWNER, ADMIN, MEMBER
    val contribution: Long,
    val joinedAt: Long
)

@Entity(
    tableName = "cp_partnerships",
    indices = [Index(value = ["userId"], unique = true)]
)
data class CpPartnershipEntity(
    @PrimaryKey val userId: String, // Current user ID
    val partnerId: String,
    val partnerName: String,
    val partnerAvatar: String?,
    val partnerLevel: Int,
    val cpLevel: Int,
    val intimacyPoints: Long,
    val daysTogether: Int,
    val startedAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "profile_visitors",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["visitedAt"])
    ]
)
data class ProfileVisitorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String, // Visitor's ID
    val userName: String,
    val userAvatar: String?,
    val userLevel: Int,
    val vipTier: Int,
    val isOnline: Boolean,
    val visitedAt: Long,
    val visitCount: Int = 1
)

@Entity(
    tableName = "medals",
    indices = [Index(value = ["category"])]
)
data class MedalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconUrl: String,
    val category: String, // ACHIEVEMENT, LEVEL, VIP, EVENT, SPECIAL
    val rarity: String, // COMMON, RARE, EPIC, LEGENDARY
    val isUnlocked: Boolean,
    val progress: Float?,
    val progressTarget: Int?,
    val unlockedAt: Long?,
    val howToEarn: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "events",
    indices = [
        Index(value = ["status"]),
        Index(value = ["type"])
    ]
)
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val bannerUrl: String,
    val startTime: Long,
    val endTime: Long,
    val type: String, // "RECHARGE", "LUCKY_DRAW", "ROOM_SUPPORT"
    val status: String, // "UPCOMING", "ACTIVE", "ENDED"
    val isParticipating: Boolean,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "faqs",
    indices = [Index(value = ["category"])]
)
data class FaqEntity(
    @PrimaryKey val id: String,
    val question: String,
    val answer: String,
    val category: String,
    val order: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "admins",
    indices = [Index(value = ["adminLevel"]), Index(value = ["country"])]
)
data class AdminEntity(
    @PrimaryKey val userId: String,
    val email: String,
    val name: String,
    val adminLevel: String, // OWNER, COUNTRY_ADMIN, ADMIN_LEVEL_1, ADMIN_LEVEL_2, ADMIN_LEVEL_3, CUSTOMER_SUPPORT
    val country: String?,
    val reportsTo: String?,
    val isActive: Boolean,
    val assignedAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "guide_applications",
    indices = [Index(value = ["userId"]), Index(value = ["status"])]
)
data class GuideApplicationEntity(
    @PrimaryKey val applicationId: String,
    val userId: String,
    val status: String, // NOT_APPLIED, PENDING, APPROVED, REJECTED, SUSPENDED
    val appliedAt: Long,
    val reviewedAt: Long?,
    val rejectionReason: String?,
// ============================================
// Week 4: Advanced Features Entities
// ============================================

@Entity(
    tableName = "notifications",
    indices = [
        Index(value = ["isRead"]),
        Index(value = ["type"]),
        Index(value = ["createdAt"])
    ]
)
data class NotificationEntity(
    @PrimaryKey val id: String,
    val type: String, // MENTION, GIFT, FOLLOW, SYSTEM, etc.
    val title: String,
    val message: String,
    val imageUrl: String?,
    val deepLink: String?,
    val isRead: Boolean,
    val createdAt: Long
)

@Entity(
    tableName = "songs",
    indices = [Index(value = ["title"])]
)
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val albumArt: String,
    val duration: Long,
    val streamUrl: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "earning_targets",
    indices = [Index(value = ["isActive"])]
)
data class EarningTargetEntity(
    @PrimaryKey val targetId: String,
    val name: String,
    val requiredDiamonds: Long,
    val cashReward: Double,
    val isActive: Boolean,
    val validFrom: Long,
    val validUntil: Long,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "cashout_requests",
    indices = [Index(value = ["userId"]), Index(value = ["status"])]
)
data class CashoutRequestEntity(
    @PrimaryKey val requestId: String,
    val userId: String,
    val amount: Double,
    val diamonds: Long,
    val status: String, // PENDING_CLEARANCE, PENDING_APPROVAL, APPROVED, PROCESSING, COMPLETED, REJECTED
    val requestedAt: Long,
    val clearanceDate: Long,
    val approvedBy: String?,
    val approvedAt: Long?,
    val rejectionReason: String?,
    val paymentMethod: String,
    val cachedAt: Long = System.currentTimeMillis()
    tableName = "playlists",
    indices = [Index(value = ["createdBy"])]
)
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val coverUrl: String,
    val createdBy: String,
    val songCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "playlist_songs",
    indices = [
        Index(value = ["playlistId"]),
        Index(value = ["songId"])
    ]
)
data class PlaylistSongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playlistId: String,
    val songId: String,
    val position: Int
)
