package com.hermeticvm.linkahest.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hermeticvm.linkahest.data.models.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val NITTER_INSTANCE_KEY = stringPreferencesKey("nitter_instance")
        private val CUSTOM_NITTER_KEY = stringPreferencesKey("custom_nitter")
        private val INVIDIOUS_INSTANCE_KEY = stringPreferencesKey("invidious_instance")
        private val CUSTOM_INVIDIOUS_KEY = stringPreferencesKey("custom_invidious")
        private val REDLIB_INSTANCE_KEY = stringPreferencesKey("redlib_instance")
        private val CUSTOM_REDLIB_KEY = stringPreferencesKey("custom_redlib")
        private val SCRIBE_INSTANCE_KEY = stringPreferencesKey("scribe_instance")
        private val CUSTOM_SCRIBE_KEY = stringPreferencesKey("custom_scribe")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val HISTORY_ENABLED_KEY = booleanPreferencesKey("history_enabled")
    }
    
    val userSettings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            selectedNitterInstance = preferences[NITTER_INSTANCE_KEY] ?: "farside.link/nitter",
            customNitterInstance = preferences[CUSTOM_NITTER_KEY] ?: "",
            selectedInvidiousInstance = preferences[INVIDIOUS_INSTANCE_KEY] ?: "farside.link/invidious",
            customInvidiousInstance = preferences[CUSTOM_INVIDIOUS_KEY] ?: "",
            selectedRedlibInstance = preferences[REDLIB_INSTANCE_KEY] ?: "farside.link/redlib",
            customRedlibInstance = preferences[CUSTOM_REDLIB_KEY] ?: "",
            selectedScribeInstance = preferences[SCRIBE_INSTANCE_KEY] ?: "farside.link/scribe",
            customScribeInstance = preferences[CUSTOM_SCRIBE_KEY] ?: "",
            themeMode = preferences[THEME_MODE_KEY] ?: "system",
            historyEnabled = preferences[HISTORY_ENABLED_KEY] ?: false
        )
    }
    
    suspend fun updateNitterInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[NITTER_INSTANCE_KEY] = instance
        }
    }
    
    suspend fun updateCustomNitterInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOM_NITTER_KEY] = instance
        }
    }
    
    suspend fun updateInvidiousInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[INVIDIOUS_INSTANCE_KEY] = instance
        }
    }
    
    suspend fun updateCustomInvidiousInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOM_INVIDIOUS_KEY] = instance
        }
    }
    
    suspend fun updateRedlibInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[REDLIB_INSTANCE_KEY] = instance
        }
    }
    
    suspend fun updateCustomRedlibInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOM_REDLIB_KEY] = instance
        }
    }

    suspend fun updateScribeInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[SCRIBE_INSTANCE_KEY] = instance
        }
    }

    suspend fun updateCustomScribeInstance(instance: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOM_SCRIBE_KEY] = instance
        }
    }

    suspend fun updateThemeMode(themeMode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode
        }
    }

    suspend fun updateHistoryEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HISTORY_ENABLED_KEY] = enabled
        }
    }
}
