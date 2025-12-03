package com.aura.voicechat.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aura.voicechat.data.model.MessagePrivacy
import com.aura.voicechat.data.model.ProfileVisibility
import com.aura.voicechat.data.model.UserPreferences
import com.aura.voicechat.data.remote.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Preferences Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */

// DataStore extension
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService
) {
    
    // Preference keys
    private object PreferenceKeys {
        val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications_enabled")
        val MESSAGE_NOTIFICATIONS = booleanPreferencesKey("message_notifications")
        val GIFT_NOTIFICATIONS = booleanPreferencesKey("gift_notifications")
        val FOLLOW_NOTIFICATIONS = booleanPreferencesKey("follow_notifications")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val SHOW_ONLINE_STATUS = booleanPreferencesKey("show_online_status")
        val ALLOW_MESSAGES_FROM = stringPreferencesKey("allow_messages_from")
        val PROFILE_VISIBILITY = stringPreferencesKey("profile_visibility")
    }
    
    /**
     * Get user preferences from DataStore
     */
    fun getPreferences(): Flow<UserPreferences> {
        return context.dataStore.data.map { preferences ->
            UserPreferences(
                pushNotificationsEnabled = preferences[PreferenceKeys.PUSH_NOTIFICATIONS] ?: true,
                messageNotifications = preferences[PreferenceKeys.MESSAGE_NOTIFICATIONS] ?: true,
                giftNotifications = preferences[PreferenceKeys.GIFT_NOTIFICATIONS] ?: true,
                followNotifications = preferences[PreferenceKeys.FOLLOW_NOTIFICATIONS] ?: true,
                soundEnabled = preferences[PreferenceKeys.SOUND_ENABLED] ?: true,
                vibrationEnabled = preferences[PreferenceKeys.VIBRATION_ENABLED] ?: true,
                showOnlineStatus = preferences[PreferenceKeys.SHOW_ONLINE_STATUS] ?: true,
                allowMessagesFrom = MessagePrivacy.valueOf(
                    preferences[PreferenceKeys.ALLOW_MESSAGES_FROM] ?: MessagePrivacy.EVERYONE.name
                ),
                profileVisibility = ProfileVisibility.valueOf(
                    preferences[PreferenceKeys.PROFILE_VISIBILITY] ?: ProfileVisibility.PUBLIC.name
                )
            )
        }
    }
    
    /**
     * Update user preferences locally and sync with server
     */
    fun updatePreferences(preferences: UserPreferences): Flow<Result<UserPreferences>> = flow {
        try {
            // Save to DataStore first
            context.dataStore.edit { prefs ->
                prefs[PreferenceKeys.PUSH_NOTIFICATIONS] = preferences.pushNotificationsEnabled
                prefs[PreferenceKeys.MESSAGE_NOTIFICATIONS] = preferences.messageNotifications
                prefs[PreferenceKeys.GIFT_NOTIFICATIONS] = preferences.giftNotifications
                prefs[PreferenceKeys.FOLLOW_NOTIFICATIONS] = preferences.followNotifications
                prefs[PreferenceKeys.SOUND_ENABLED] = preferences.soundEnabled
                prefs[PreferenceKeys.VIBRATION_ENABLED] = preferences.vibrationEnabled
                prefs[PreferenceKeys.SHOW_ONLINE_STATUS] = preferences.showOnlineStatus
                prefs[PreferenceKeys.ALLOW_MESSAGES_FROM] = preferences.allowMessagesFrom.name
                prefs[PreferenceKeys.PROFILE_VISIBILITY] = preferences.profileVisibility.name
            }
            
            // Sync with server
            val response = apiService.updateUserPreferences(preferences)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!.preferences))
            } else {
                // Local save succeeded, but server sync failed - still return success
                emit(Result.success(preferences))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Fetch preferences from server and update local cache
     */
    fun syncPreferences(): Flow<Result<UserPreferences>> = flow {
        try {
            val response = apiService.getUserPreferences()
            if (response.isSuccessful && response.body() != null) {
                val serverPreferences = response.body()!!.preferences
                
                // Update DataStore with server data
                context.dataStore.edit { prefs ->
                    prefs[PreferenceKeys.PUSH_NOTIFICATIONS] = serverPreferences.pushNotificationsEnabled
                    prefs[PreferenceKeys.MESSAGE_NOTIFICATIONS] = serverPreferences.messageNotifications
                    prefs[PreferenceKeys.GIFT_NOTIFICATIONS] = serverPreferences.giftNotifications
                    prefs[PreferenceKeys.FOLLOW_NOTIFICATIONS] = serverPreferences.followNotifications
                    prefs[PreferenceKeys.SOUND_ENABLED] = serverPreferences.soundEnabled
                    prefs[PreferenceKeys.VIBRATION_ENABLED] = serverPreferences.vibrationEnabled
                    prefs[PreferenceKeys.SHOW_ONLINE_STATUS] = serverPreferences.showOnlineStatus
                    prefs[PreferenceKeys.ALLOW_MESSAGES_FROM] = serverPreferences.allowMessagesFrom.name
                    prefs[PreferenceKeys.PROFILE_VISIBILITY] = serverPreferences.profileVisibility.name
                }
                
                emit(Result.success(serverPreferences))
            } else {
                emit(Result.failure(Exception("Failed to fetch preferences: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Clear all preferences
     */
    suspend fun clearPreferences() {
        context.dataStore.edit { it.clear() }
    }
}
