package com.yash091099.ChiragFarmersApp.data.local

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val chiragDataStore: ChiragDataStore
) {
    fun getAccessTokenSync(): String? {
        return runBlocking {
            chiragDataStore.getAuthToken().first()
        }
    }

    fun getRefreshTokenSync(): String? {
        return runBlocking {
            chiragDataStore.getRefreshToken().first()
        }
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        chiragDataStore.saveAuthToken(accessToken)
        chiragDataStore.saveRefreshToken(refreshToken)
    }

    suspend fun clearTokens() {
        chiragDataStore.clearAuthData()
    }
}
