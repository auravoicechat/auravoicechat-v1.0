package com.aura.voicechat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aura.voicechat.data.local.dao.*
import com.aura.voicechat.data.local.entity.*

/**
 * Aura Voice Chat Room Database
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Local database for caching and offline support
 * Version 1 - Initial schema for Week 2 features
 */
@Database(
    entities = [
        UserEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        FriendEntity::class,
        FriendRequestEntity::class,
        GiftEntity::class,
        FamilyEntity::class,
        FamilyMemberEntity::class,
        CpPartnershipEntity::class,
        ProfileVisitorEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    // DAOs
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
    abstract fun giftDao(): GiftDao
    abstract fun familyDao(): FamilyDao
    abstract fun cpDao(): CpDao
    abstract fun visitorDao(): VisitorDao
    
    companion object {
        const val DATABASE_NAME = "aura_voice_chat.db"
    }
}
