package com.aura.voicechat.di

import com.aura.voicechat.data.repository.*
import com.aura.voicechat.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Dependency Injection Module
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Note: AuthRepository binding is handled separately to avoid duplicate binding conflicts.
 * If you see duplicate binding errors, ensure AuthBindModule.kt exists in this package
 * with the bindAuthRepository method, OR uncomment the binding below and delete AuthBindModule.kt.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    // AuthRepository binding removed - should be in AuthBindModule.kt
    // If AuthBindModule.kt doesn't exist, uncomment below:
    // @Binds
    // @Singleton
    // abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindWalletRepository(impl: WalletRepositoryImpl): WalletRepository
    
    @Binds
    @Singleton
    abstract fun bindRewardsRepository(impl: RewardsRepositoryImpl): RewardsRepository
    
    @Binds
    @Singleton
    abstract fun bindKycRepository(impl: KycRepositoryImpl): KycRepository
}
