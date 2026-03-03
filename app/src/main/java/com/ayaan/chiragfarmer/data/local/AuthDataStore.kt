package com.ayaan.chiragfarmer.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

class AuthDataStore(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_PHONE_KEY = stringPreferencesKey("user_phone")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val PROFILE_COMPLETE_KEY = booleanPreferencesKey("profile_complete")
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserData(userId: String, phone: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_PHONE_KEY] = phone
            preferences[USER_ROLE_KEY] = role
        }
    }

    suspend fun saveUserSession(token: String, userId: String, phone: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
            preferences[USER_PHONE_KEY] = phone
            preferences[USER_ROLE_KEY] = role
        }
    }

    fun getAuthToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getUserRole(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE_KEY]
        }
    }

    fun getUserPhone(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_PHONE_KEY]
        }
    }

    suspend fun saveProfileStatus(isComplete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PROFILE_COMPLETE_KEY] = isComplete
        }
    }

    fun getProfileStatus(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PROFILE_COMPLETE_KEY] ?: false
        }
    }

    suspend fun clearProfileStatus() {
        context.dataStore.edit { preferences ->
            preferences.remove(PROFILE_COMPLETE_KEY)
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}