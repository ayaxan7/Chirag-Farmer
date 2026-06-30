package com.yash091099.ChiragFarmersApp.data.remote

import com.google.gson.Gson
import com.yash091099.ChiragFarmersApp.data.local.TokenManager
import com.yash091099.ChiragFarmersApp.data.model.auth.AuthResponse
import com.yash091099.ChiragFarmersApp.data.model.auth.RefreshTokenRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.TokenRefreshData
import com.yash091099.ChiragFarmersApp.di.BaseOkHttpClient
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @BaseOkHttpClient private val okHttpClient: OkHttpClient
) : Authenticator {

    private val gson = Gson()
    private val jsonMediaType = "application/json".toMediaType()

    override fun authenticate(route: Route?, response: Response): Request? {
        val requestPath = response.request.url.encodedPath
        if (requestPath.contains("refresh-token")) {
            Timber.d("Refresh token endpoint itself returned 401, not retrying")
            return null
        }

        val refreshToken = tokenManager.getRefreshTokenSync()
        if (refreshToken.isNullOrEmpty()) {
            Timber.d("No refresh token available, clearing auth data")
            runBlocking { tokenManager.clearTokens() }
            return null
        }

        return try {
            val refreshBody = gson.toJson(RefreshTokenRequest(refreshToken = refreshToken))
            val refreshRequest = Request.Builder()
                .url(response.request.url.scheme + "://" + response.request.url.host + "/api/msg91/refresh-token")
                .post(refreshBody.toRequestBody(jsonMediaType))
                .build()

            val refreshResponse = okHttpClient.newCall(refreshRequest).execute()

            if (!refreshResponse.isSuccessful) {
                Timber.d("Token refresh failed with code: ${refreshResponse.code}")
                runBlocking { tokenManager.clearTokens() }
                return null
            }

            val responseBody = refreshResponse.body?.string()
            val authResponse = gson.fromJson(responseBody, AuthResponse::class.java)

            if (authResponse.success && authResponse.data != null) {
                val data = gson.fromJson(gson.toJson(authResponse.data), TokenRefreshData::class.java)
                runBlocking {
                    tokenManager.saveTokens(data.token, data.refreshToken)
                }

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${data.token}")
                    .build()
            } else {
                Timber.d("Token refresh returned unsuccessful: ${authResponse.message}")
                runBlocking { tokenManager.clearTokens() }
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Token refresh failed")
            runBlocking { tokenManager.clearTokens() }
            null
        }
    }
}
