package com.aura.voicechat.di

import android.content.Context
import androidx.room.Room
import com.aura.voicechat.data.local.AppDatabase
import com.aura.voicechat.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database Module
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Provides Room database and DAO instances
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development, remove in production
            .build()
    }
    
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    @Singleton
    fun provideConversationDao(database: AppDatabase): ConversationDao {
        return database.conversationDao()
    }
    
    @Provides
    @Singleton
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }
    
    @Provides
    @Singleton
    fun provideFriendDao(database: AppDatabase): FriendDao {
        return database.friendDao()
    }
    
    @Provides
    @Singleton
    fun provideGiftDao(database: AppDatabase): GiftDao {
        return database.giftDao()
    }
    
    @Provides
    @Singleton
    fun provideFamilyDao(database: AppDatabase): FamilyDao {
        return database.familyDao()
    }
    
    @Provides
    @Singleton
    fun provideCpDao(database: AppDatabase): CpDao {
        return database.cpDao()
    }
    
    @Provides
    @Singleton
    fun provideVisitorDao(database: AppDatabase): VisitorDao {
        return database.visitorDao()
    }
}
