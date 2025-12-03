package com.aura.voicechat.data.local.dao

import androidx.room.*
import com.aura.voicechat.data.local.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * Database Access Objects (DAOs)
 * Developer: Hawkaye Visions LTD — Pakistan
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUser(userId: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserOnce(userId: String): UserEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    @Query("DELETE FROM users WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY lastMessageTime DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>
    
    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    fun getConversation(conversationId: String): Flow<ConversationEntity?>
    
    @Query("SELECT * FROM conversations WHERE otherUserId = :userId LIMIT 1")
    suspend fun getConversationByUserId(userId: String): ConversationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)
    
    @Update
    suspend fun updateConversation(conversation: ConversationEntity)
    
    @Delete
    suspend fun deleteConversation(conversation: ConversationEntity)
    
    @Query("UPDATE conversations SET unreadCount = 0 WHERE conversationId = :conversationId")
    suspend fun markAsRead(conversationId: String)
    
    @Query("SELECT SUM(unreadCount) FROM conversations")
    fun getTotalUnreadCount(): Flow<Int>
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessages(conversationId: String, limit: Int = 50): List<MessageEntity>
    
    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessage(messageId: String): MessageEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
    
    @Update
    suspend fun updateMessage(message: MessageEntity)
    
    @Delete
    suspend fun deleteMessage(message: MessageEntity)
    
    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteConversationMessages(conversationId: String)
    
    @Query("UPDATE messages SET isRead = 1 WHERE conversationId = :conversationId AND receiverId = :userId")
    suspend fun markConversationAsRead(conversationId: String, userId: String)
    
    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId AND receiverId = :userId AND isRead = 0")
    fun getUnreadCount(conversationId: String, userId: String): Flow<Int>
}

@Dao
interface FriendDao {
    @Query("SELECT * FROM friends ORDER BY isOnline DESC, name ASC")
    fun getAllFriends(): Flow<List<FriendEntity>>
    
    @Query("SELECT * FROM friends WHERE isOnline = 1 ORDER BY name ASC")
    fun getOnlineFriends(): Flow<List<FriendEntity>>
    
    @Query("SELECT * FROM friends WHERE userId = :userId")
    suspend fun getFriend(userId: String): FriendEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: FriendEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<FriendEntity>)
    
    @Delete
    suspend fun deleteFriend(friend: FriendEntity)
    
    @Query("DELETE FROM friends WHERE userId = :userId")
    suspend fun deleteFriendById(userId: String)
    
    @Query("SELECT * FROM friend_requests WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingRequests(): Flow<List<FriendRequestEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendRequest(request: FriendRequestEntity)
    
    @Query("UPDATE friend_requests SET status = :status WHERE requestId = :requestId")
    suspend fun updateRequestStatus(requestId: String, status: String)
    
    @Delete
    suspend fun deleteFriendRequest(request: FriendRequestEntity)
}

@Dao
interface GiftDao {
    @Query("SELECT * FROM gifts WHERE isAvailable = 1 ORDER BY category, price")
    fun getAllGifts(): Flow<List<GiftEntity>>
    
    @Query("SELECT * FROM gifts WHERE category = :category AND isAvailable = 1 ORDER BY price")
    fun getGiftsByCategory(category: String): Flow<List<GiftEntity>>
    
    @Query("SELECT * FROM gifts WHERE giftId = :giftId")
    suspend fun getGift(giftId: String): GiftEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: GiftEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifts(gifts: List<GiftEntity>)
    
    @Query("DELETE FROM gifts WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface FamilyDao {
    @Query("SELECT * FROM families WHERE familyId = :familyId")
    fun getFamily(familyId: String): Flow<FamilyEntity?>
    
    @Query("SELECT * FROM families WHERE rank IS NOT NULL ORDER BY rank ASC LIMIT :limit")
    fun getTopFamilies(limit: Int = 100): Flow<List<FamilyEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamily(family: FamilyEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilies(families: List<FamilyEntity>)
    
    @Query("SELECT * FROM family_members WHERE familyId = :familyId ORDER BY role, contribution DESC")
    fun getFamilyMembers(familyId: String): Flow<List<FamilyMemberEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(member: FamilyMemberEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMembers(members: List<FamilyMemberEntity>)
    
    @Query("DELETE FROM family_members WHERE familyId = :familyId AND userId = :userId")
    suspend fun removeFamilyMember(familyId: String, userId: String)
}

@Dao
interface CpDao {
    @Query("SELECT * FROM cp_partnerships WHERE userId = :userId")
    fun getCpPartnership(userId: String): Flow<CpPartnershipEntity?>
    
    @Query("SELECT * FROM cp_partnerships WHERE userId = :userId")
    suspend fun getCpPartnershipOnce(userId: String): CpPartnershipEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCpPartnership(partnership: CpPartnershipEntity)
    
    @Delete
    suspend fun deleteCpPartnership(partnership: CpPartnershipEntity)
    
    @Query("DELETE FROM cp_partnerships WHERE userId = :userId")
    suspend fun dissolveCp(userId: String)
}

@Dao
interface VisitorDao {
    @Query("SELECT * FROM profile_visitors ORDER BY visitedAt DESC LIMIT :limit")
    fun getRecentVisitors(limit: Int = 100): Flow<List<ProfileVisitorEntity>>
    
    @Query("SELECT COUNT(*) FROM profile_visitors")
    fun getTotalVisitorsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM profile_visitors WHERE visitedAt >= :timestamp")
    fun getTodayVisitorsCount(timestamp: Long): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitor(visitor: ProfileVisitorEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitors(visitors: List<ProfileVisitorEntity>)
    
    @Query("DELETE FROM profile_visitors WHERE visitedAt < :timestamp")
    suspend fun deleteOldVisitors(timestamp: Long)
}
