package com.aura.voicechat.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Locale Manager for Multi-language Support
 * Developer: Hawkaye Visions LTD — Pakistan
 */

private val Context.localeDataStore: DataStore<Preferences> by preferencesDataStore(name = "locale_preferences")

@Singleton
class LocaleManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val languageKey = stringPreferencesKey("selected_language")
    
    companion object {
        const val ENGLISH = "en"
        const val SPANISH = "es"
        const val HINDI = "hi"
        const val ARABIC = "ar"
        const val PORTUGUESE = "pt"
        const val INDONESIAN = "id"
        
        val SUPPORTED_LANGUAGES = listOf(
            Language(ENGLISH, "English", "🇬🇧"),
            Language(SPANISH, "Español", "🇪🇸"),
            Language(HINDI, "हिन्दी", "🇮🇳"),
            Language(ARABIC, "العربية", "🇸🇦"),
            Language(PORTUGUESE, "Português", "🇧🇷"),
            Language(INDONESIAN, "Bahasa Indonesia", "🇮🇩")
        )
    }
    
    /**
     * Get current selected language
     */
    fun getSelectedLanguage(): Flow<String> {
        return context.localeDataStore.data.map { preferences ->
            preferences[languageKey] ?: ENGLISH
        }
    }
    
    /**
     * Set application language
     */
    suspend fun setLanguage(languageCode: String) {
        context.localeDataStore.edit { preferences ->
            preferences[languageKey] = languageCode
        }
    }
    
    /**
     * Apply locale to context
     */
    fun applyLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLocales(LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        
        return context.createConfigurationContext(config)
    }
    
    /**
     * Check if language is RTL (Right-to-Left)
     */
    fun isRTL(languageCode: String): Boolean {
        return languageCode == ARABIC
    }
    
    /**
     * Get display name for language code
     */
    fun getLanguageDisplayName(languageCode: String): String {
        return SUPPORTED_LANGUAGES.find { it.code == languageCode }?.name ?: "English"
    }
}

data class Language(
    val code: String,
    val name: String,
    val flag: String
)
