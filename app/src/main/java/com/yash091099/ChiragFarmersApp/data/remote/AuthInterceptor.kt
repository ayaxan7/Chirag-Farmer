package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    private val skipAuthPaths = setOf(
        "/api/msg91/send-otp",
        "/api/msg91/verify-otp",
        "/api/msg91/send-registration-otp",
        "/api/msg91/register",
        "/api/msg91/refresh-token"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestPath = originalRequest.url.encodedPath

        if (skipAuthPaths.any { requestPath.contains(it) }) {
            return chain.proceed(originalRequest)
        }

        if (originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest)
        }

        val accessToken = tokenManager.getAccessTokenSync()
        if (accessToken.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
