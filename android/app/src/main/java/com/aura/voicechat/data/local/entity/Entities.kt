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
