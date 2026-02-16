package com.ayaan.chiragfarmer.data.repository

import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.data.model.auth.AddBusinessInfoRequest
import com.ayaan.chiragfarmer.data.model.auth.AuthResponse
import com.ayaan.chiragfarmer.data.model.auth.RegisterRequest
import com.ayaan.chiragfarmer.data.model.auth.SendOTPData
import com.ayaan.chiragfarmer.data.model.auth.SendOTPRequest
import com.ayaan.chiragfarmer.data.model.auth.UserDetailsData
import com.ayaan.chiragfarmer.data.model.auth.VerifyOTPData
import com.ayaan.chiragfarmer.data.model.auth.VerifyOTPRequest
import com.ayaan.chiragfarmer.data.remote.AuthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val authDataStore: AuthDataStore
) {

    suspend fun sendLoginOTP(phone: String): Result<AuthResponse<SendOTPData>> {
        return try {
            val response = apiService.sendLoginOTP(SendOTPRequest(phone = phone, role = "farmer"))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendRegistrationOTP(phone: String): Result<AuthResponse<SendOTPData>> {
        return try {
            val response = apiService.sendRegistrationOTP(SendOTPRequest(phone = phone, role = "farmer"))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyLoginOTP(phone: String, otp: String, requestId: String): Result<AuthResponse<VerifyOTPData>> {
        return try {
            val response = apiService.verifyLoginOTP(
                VerifyOTPRequest(
                    phone = phone,
                    otp = otp,
                    requestId = requestId,
                    role = "farmer"
                )
            )

            // Save token and user data if verification is successful
            if (response.success && response.data?.verified == true) {
                response.data.token?.let { token ->
                    response.data.user?.let { user ->
                        authDataStore.saveUserSession(token, user.id, user.phone, user.role)
                    }
                }
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(phone: String, otp: String, requestId: String): Result<AuthResponse<VerifyOTPData>> {
        return try {
            val response = apiService.register(
                RegisterRequest(
                    phone = phone,
                    otp = otp,
                    requestId = requestId,
                    role = "farmer"
                )
            )

            // Save token and user data if registration is successful
            if (response.success && response.data?.verified == true) {
                response.data.token?.let { token ->
                    response.data.user?.let { user ->
                        authDataStore.saveUserSession(token, user.id, user.phone, user.role)
                    }
                }
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAuthToken(): Flow<String?> {
        return authDataStore.getAuthToken()
    }

    fun getUserRole(): Flow<String?> {
        return authDataStore.getUserRole()
    }

    suspend fun getUserDetails(): Result<AuthResponse<UserDetailsData>> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.getUserDetails("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addBusinessInfo(request: AddBusinessInfoRequest): Result<AuthResponse<UserDetailsData>> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.addBusinessInfo("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        authDataStore.clearAuthData()
    }
}

