package com.yash091099.ChiragFarmersApp.data.local

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

class ChiragDataStore(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_PHONE_KEY = stringPreferencesKey("user_phone")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val PROFILE_COMPLETE_KEY = booleanPreferencesKey("profile_complete")
        private val DEFAULT_LOCATION_KEY = stringPreferencesKey("default_location")
        private val LOCATION_UPDATED_ON_LAUNCH_KEY = booleanPreferencesKey("location_updated_on_launch")
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
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

    suspend fun saveDefaultLocation(locationJson: String) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_LOCATION_KEY] = locationJson
        }
    }

    fun getDefaultLocation(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[DEFAULT_LOCATION_KEY]
        }
    }

    suspend fun saveLocationUpdatedOnLaunch(updated: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION_UPDATED_ON_LAUNCH_KEY] = updated
        }
    }

    fun getLocationUpdatedOnLaunch(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[LOCATION_UPDATED_ON_LAUNCH_KEY] ?: false
        }
    }

    suspend fun saveDeviceId(deviceId: String) {
        context.dataStore.edit { preferences ->
            preferences[DEVICE_ID_KEY] = deviceId
        }
    }

    fun getDeviceId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[DEVICE_ID_KEY]
        }
    }
}