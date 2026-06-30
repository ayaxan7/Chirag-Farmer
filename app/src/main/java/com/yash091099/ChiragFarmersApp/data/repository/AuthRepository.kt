package com.yash091099.ChiragFarmersApp.data.repository

import timber.log.Timber
import com.google.gson.Gson
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.local.TokenManager
import com.yash091099.ChiragFarmersApp.data.model.auth.AddBusinessInfoRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.AuthResponse
import com.yash091099.ChiragFarmersApp.data.model.auth.FarmerProfileResponse
import com.yash091099.ChiragFarmersApp.data.model.auth.LogoutRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.RefreshTokenRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.RegisterRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPData
import com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.TokenRefreshData
import com.yash091099.ChiragFarmersApp.data.model.auth.UpdateProfileData
import com.yash091099.ChiragFarmersApp.data.model.auth.UserDetailsData
import com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPData
import com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPRequest
import com.yash091099.ChiragFarmersApp.data.remote.AuthApiService
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDeviceTokenRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDeviceTokenResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateLanguageRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateLanguageResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteDeviceTokenRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteDeviceTokenResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.DefaultLocationData
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.SetDefaultAddressRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.SetDefaultAddressResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val chiragDataStore: ChiragDataStore,
    private val tokenManager: TokenManager
) {

    suspend fun sendLoginOTP(phone: String): Result<AuthResponse<SendOTPData>> {
        return try {
            val response = apiService.sendLoginOTP(SendOTPRequest(phone = phone, role = "farmer"))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun sendRegistrationOTP(phone: String): Result<AuthResponse<SendOTPData>> {
        return try {
            val response = apiService.sendRegistrationOTP(SendOTPRequest(phone = phone, role = "farmer"))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
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

            if (response.success && response.data?.verified == true) {
                response.data.token?.let { token ->
                    response.data.user?.let { user ->
                        chiragDataStore.saveUserSession(token, user.id, user.phone, user.role)
                        chiragDataStore.saveProfileStatus(false)
                    }
                }
                val refreshToken = response.data.refreshToken
                if (!refreshToken.isNullOrEmpty()) {
                    chiragDataStore.saveRefreshToken(refreshToken)
                }
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
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

            if (response.success && response.data?.verified == true) {
                response.data.token?.let { token ->
                    response.data.user?.let { user ->
                        chiragDataStore.saveUserSession(token, user.id, user.phone, user.role)
                        chiragDataStore.saveProfileStatus(false)
                    }
                }
                val refreshToken = response.data.refreshToken
                if (!refreshToken.isNullOrEmpty()) {
                    chiragDataStore.saveRefreshToken(refreshToken)
                }
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
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
            Result.failure(Exception(getErrorMessage(e)))
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
            Result.failure(Exception(getErrorMessage(e)))
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
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    fun getProfileStatus(): Flow<Boolean> {
        return chiragDataStore.getProfileStatus()
    }

    suspend fun getFarmerProfile(): Result<FarmerProfileResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }

            val response = apiService.getFarmerProfile("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun updateProfile(changedFields: Map<String, String>): Result<AuthResponse<UpdateProfileData>> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }

            val response = apiService.updateProfile("Bearer $token", changedFields)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun logout() {
        try {
            val refreshToken = chiragDataStore.getRefreshToken().first()
            if (!refreshToken.isNullOrEmpty()) {
                apiService.logout(LogoutRequest(refreshToken = refreshToken))
            }
        } catch (e: Exception) {
            Timber.w("Logout API call failed: ${e.message}")
        }

        try {
            val deviceId = chiragDataStore.getDeviceId().first()
            if (!deviceId.isNullOrEmpty()) {
                deleteDeviceToken(deviceId).fold(
                    onSuccess = {
                        Timber.d("FCM token deleted from server successfully")
                    },
                    onFailure = { exception ->
                        Timber.w("Failed to delete FCM token: ${exception.message}")
                    }
                )
            }
        } catch (e: Exception) {
            Timber.e("Error during FCM token deletion: ${e.message}")
        }

        chiragDataStore.clearAuthData()
        chiragDataStore.saveLocationUpdatedOnLaunch(false)
    }

    suspend fun refreshToken(): Result<AuthResponse<TokenRefreshData>> {
        return try {
            val token = chiragDataStore.getRefreshToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No refresh token available"))
            }
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken = token))
            if (response.success && response.data != null) {
                val data = response.data
                chiragDataStore.saveAuthToken(data.token)
                chiragDataStore.saveRefreshToken(data.refreshToken)
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
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
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun updateDeviceToken(deviceToken: String, deviceId: String): Result<UpdateDeviceTokenResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }

            val request = UpdateDeviceTokenRequest(
                token = deviceToken,
                deviceId = deviceId,
                deviceType = "android"
            )
            val response = apiService.updateDeviceToken("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun updatePreferredLanguage(request: UpdateLanguageRequest): Result<UpdateLanguageResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.updateLanguage("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun deleteDeviceToken(deviceId: String): Result<DeleteDeviceTokenResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }

            val request = DeleteDeviceTokenRequest(deviceId = deviceId)
            val response = apiService.deleteDeviceToken("Bearer $token", request)
            Timber.d("Device token deleted $response")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
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
                Result.success(response.data?.locations ?: emptyList())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
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
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun addDeliveryLocation(request: AddDeliveryLocationRequest): Result<AddDeliveryLocationResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.addDeliveryLocation("Bearer $token", request)

            if (response.success && response.data.isNotEmpty()) {
                // The new location becomes the default address. 
                // We update the data store with the latest added location info if applicable.
                val latestLocation = response.data.first()
                val defaultLocationData = DefaultLocationData(
                    id = latestLocation.id,
                    name = latestLocation.name,
                    receiverName = latestLocation.receiverName,
                    receiverContact = latestLocation.receiverContact,
                    addressString = latestLocation.addressString,
                    completeAddress = latestLocation.completeAddress,
                    floor = latestLocation.floor,
                    landmark = latestLocation.landmark,
                    pincode = latestLocation.pincode,
                    coordinates = latestLocation.coordinates
                )
                val locationJson = Gson().toJson(defaultLocationData)
                chiragDataStore.saveDefaultLocation(locationJson)
                chiragDataStore.saveLocationUpdatedOnLaunch(true)
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    suspend fun setDefaultAddress(addressId: String): Result<SetDefaultAddressResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No authentication token found"))
            }
            val request = SetDefaultAddressRequest(addressId)
            val response = apiService.setDefaultAddress("Bearer $token", request)

            if (response.success && response.data != null) {
                // Save the updated default location to DataStore
                val locationJson = Gson().toJson(response.data.address)
                chiragDataStore.saveDefaultLocation(locationJson)
                chiragDataStore.saveLocationUpdatedOnLaunch(true)
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
}

