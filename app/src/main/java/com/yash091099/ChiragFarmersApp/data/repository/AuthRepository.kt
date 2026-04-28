package com.yash091099.ChiragFarmersApp.data.repository

import com.google.gson.Gson
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.model.auth.AddBusinessInfoRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.AuthResponse
import com.yash091099.ChiragFarmersApp.data.model.auth.RegisterRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPData
import com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.UserDetailsData
import com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPData
import com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPRequest
import com.yash091099.ChiragFarmersApp.data.remote.AuthApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.DefaultLocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val chiragDataStore: ChiragDataStore
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
                        chiragDataStore.saveUserSession(token, user.id, user.phone, user.role)
                        chiragDataStore.saveProfileStatus(false)
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
                        chiragDataStore.saveUserSession(token, user.id, user.phone, user.role)
                        // Reset profile status to false on registration, will be updated by API call
                        chiragDataStore.saveProfileStatus(false)
                    }
                }
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAuthToken(): Flow<String?> {
        return chiragDataStore.getAuthToken()
    }

    fun getUserRole(): Flow<String?> {
        return chiragDataStore.getUserRole()
    }

    suspend fun getUserDetails(): Result<AuthResponse<UserDetailsData>> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
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
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.addBusinessInfo("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkProfileStatus(): Result<AuthResponse<Boolean>> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.checkProfileStatus("Bearer $token")

            // Save profile status to DataStore
            if (response.success && response.data != null) {
                chiragDataStore.saveProfileStatus(response.data)
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getProfileStatus(): Flow<Boolean> {
        return chiragDataStore.getProfileStatus()
    }

    suspend fun logout() {
        chiragDataStore.clearAuthData()
        chiragDataStore.saveLocationUpdatedOnLaunch(false)
    }

    suspend fun updateDefaultLocation(latitude: Double, longitude: Double): Result<UpdateDefaultLocationResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val request = UpdateDefaultLocationRequest(latitude = latitude, longitude = longitude)
            val response = apiService.updateDefaultLocation("Bearer $token", request)

            // Save the default location to DataStore if successful
            if (response.success && response.data != null) {
                val locationJson = Gson().toJson(response.data)
                chiragDataStore.saveDefaultLocation(locationJson)
                chiragDataStore.saveLocationUpdatedOnLaunch(true)
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getDefaultLocation(): Flow<String?> {
        return chiragDataStore.getDefaultLocation()
    }

    fun getLocationUpdatedOnLaunch(): Flow<Boolean> {
        return chiragDataStore.getLocationUpdatedOnLaunch()
    }

    suspend fun getFarmerAddresses(): Result<List<FarmerAddressDto>> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.getFarmerAddresses("Bearer $token")

            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDefaultDeliveryLocation(): Result<DefaultLocationData?> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.getDefaultLocation("Bearer $token")

            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

