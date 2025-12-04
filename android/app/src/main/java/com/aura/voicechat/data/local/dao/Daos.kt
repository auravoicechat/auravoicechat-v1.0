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

@Dao
interface MedalDao {
    @Query("SELECT * FROM medals ORDER BY category, rarity, name")
    fun getAllMedals(): Flow<List<MedalEntity>>
    
    @Query("SELECT * FROM medals WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedMedals(): Flow<List<MedalEntity>>
    
    @Query("SELECT * FROM medals WHERE category = :category ORDER BY rarity, name")
    fun getMedalsByCategory(category: String): Flow<List<MedalEntity>>
    
    @Query("SELECT * FROM medals WHERE id = :medalId")
    suspend fun getMedal(medalId: String): MedalEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedal(medal: MedalEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedals(medals: List<MedalEntity>)
    
    @Update
    suspend fun updateMedal(medal: MedalEntity)
    
    @Query("DELETE FROM medals WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startTime DESC")
    fun getAllEvents(): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE status = 'ACTIVE' ORDER BY endTime ASC")
    fun getActiveEvents(): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEvent(eventId: String): EventEntity?
    
    @Query("SELECT * FROM events WHERE type = :type ORDER BY startTime DESC")
    fun getEventsByType(type: String): Flow<List<EventEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    
    @Update
    suspend fun updateEvent(event: EventEntity)
    
    @Query("DELETE FROM events WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface FaqDao {
    @Query("SELECT * FROM faqs ORDER BY category, `order`")
    fun getAllFaqs(): Flow<List<FaqEntity>>
    
    @Query("SELECT * FROM faqs WHERE category = :category ORDER BY `order`")
    fun getFaqsByCategory(category: String): Flow<List<FaqEntity>>
    
    @Query("SELECT * FROM faqs WHERE id = :faqId")
    suspend fun getFaq(faqId: String): FaqEntity?
    
    @Query("SELECT * FROM faqs WHERE question LIKE '%' || :query || '%' OR answer LIKE '%' || :query || '%'")
    fun searchFaqs(query: String): Flow<List<FaqEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaq(faq: FaqEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaqs(faqs: List<FaqEntity>)
    
    @Query("DELETE FROM faqs WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins WHERE isActive = 1 ORDER BY adminLevel")
    fun getAllActiveAdmins(): Flow<List<AdminEntity>>
    
    @Query("SELECT * FROM admins WHERE userId = :userId")
    suspend fun getAdmin(userId: String): AdminEntity?
    
    @Query("SELECT * FROM admins WHERE adminLevel = :level AND isActive = 1")
    fun getAdminsByLevel(level: String): Flow<List<AdminEntity>>
    
    @Query("SELECT * FROM admins WHERE country = :country AND isActive = 1")
    fun getAdminsByCountry(country: String): Flow<List<AdminEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmins(admins: List<AdminEntity>)
    
    @Update
    suspend fun updateAdmin(admin: AdminEntity)
    
    @Query("DELETE FROM admins WHERE cachedAt < :timestamp")
// ============================================
// Week 4: Advanced Features DAOs
// ============================================

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE type = :type ORDER BY createdAt DESC")
    fun getNotificationsByType(type: String): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>
    
    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: String): NotificationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
    
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)
    
    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: String)
    
    @Query("DELETE FROM notifications WHERE createdAt < :timestamp")
    suspend fun deleteOldNotifications(timestamp: Long)
}

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchSongs(query: String): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: String): SongEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)
    
    @Delete
    suspend fun deleteSong(song: SongEntity)
    
    @Query("DELETE FROM songs WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface GuideApplicationDao {
    @Query("SELECT * FROM guide_applications WHERE userId = :userId")
    suspend fun getApplication(userId: String): GuideApplicationEntity?
    
    @Query("SELECT * FROM guide_applications WHERE status = :status ORDER BY appliedAt DESC")
    fun getApplicationsByStatus(status: String): Flow<List<GuideApplicationEntity>>
    
    @Query("SELECT * FROM guide_applications ORDER BY appliedAt DESC LIMIT :limit")
    fun getRecentApplications(limit: Int = 50): Flow<List<GuideApplicationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: GuideApplicationEntity)
    
    @Update
    suspend fun updateApplication(application: GuideApplicationEntity)
    
    @Query("DELETE FROM guide_applications WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface EarningTargetDao {
    @Query("SELECT * FROM earning_targets WHERE isActive = 1 ORDER BY requiredDiamonds ASC")
    fun getActiveTargets(): Flow<List<EarningTargetEntity>>
    
    @Query("SELECT * FROM earning_targets WHERE targetId = :targetId")
    suspend fun getTarget(targetId: String): EarningTargetEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarget(target: EarningTargetEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTargets(targets: List<EarningTargetEntity>)
    
    @Query("DELETE FROM earning_targets WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

@Dao
interface CashoutRequestDao {
    @Query("SELECT * FROM cashout_requests WHERE userId = :userId ORDER BY requestedAt DESC")
    fun getUserCashouts(userId: String): Flow<List<CashoutRequestEntity>>
    
    @Query("SELECT * FROM cashout_requests WHERE status = :status ORDER BY requestedAt DESC")
    fun getCashoutsByStatus(status: String): Flow<List<CashoutRequestEntity>>
    
    @Query("SELECT * FROM cashout_requests WHERE status = 'PENDING_APPROVAL' AND clearanceDate <= :currentTime ORDER BY clearanceDate ASC")
    fun getPendingApprovalCashouts(currentTime: Long): Flow<List<CashoutRequestEntity>>
    
    @Query("SELECT * FROM cashout_requests WHERE requestId = :requestId")
    suspend fun getCashout(requestId: String): CashoutRequestEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashout(cashout: CashoutRequestEntity)
    
    @Update
    suspend fun updateCashout(cashout: CashoutRequestEntity)
    
    @Query("DELETE FROM cashout_requests WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
interface PlaylistDao {
    @Query("SELECT * FROM playlists WHERE createdBy = :userId ORDER BY createdAt DESC")
    fun getUserPlaylists(userId: String): Flow<List<PlaylistEntity>>
    
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: String): PlaylistEntity?
    
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)
    
    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}

@Dao
interface PlaylistSongDao {
    @Query("SELECT s.* FROM songs s INNER JOIN playlist_songs ps ON s.id = ps.songId WHERE ps.playlistId = :playlistId ORDER BY ps.position ASC")
    fun getPlaylistSongs(playlistId: String): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId ORDER BY position ASC")
    fun getPlaylistSongEntries(playlistId: String): Flow<List<PlaylistSongEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSong(playlistSong: PlaylistSongEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongs(playlistSongs: List<PlaylistSongEntity>)
    
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removePlaylistSong(playlistId: String, songId: String)
    
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun clearPlaylist(playlistId: String)
}
