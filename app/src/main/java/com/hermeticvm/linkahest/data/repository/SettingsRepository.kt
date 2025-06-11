package com.hermeticvm.linkahest.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
    }
    
    val userSettings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            selectedNitterInstance = preferences[NITTER_INSTANCE_KEY] ?: "nitter.net",
            customNitterInstance = preferences[CUSTOM_NITTER_KEY] ?: "",
            selectedInvidiousInstance = preferences[INVIDIOUS_INSTANCE_KEY] ?: "inv.nadeko.net",
            customInvidiousInstance = preferences[CUSTOM_INVIDIOUS_KEY] ?: "",
            selectedRedlibInstance = preferences[REDLIB_INSTANCE_KEY] ?: "rl.bloat.cat",
            customRedlibInstance = preferences[CUSTOM_REDLIB_KEY] ?: ""
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
}