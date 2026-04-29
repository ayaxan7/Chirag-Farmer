package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.model.auth.AddBusinessInfoRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.AuthResponse
import com.yash091099.ChiragFarmersApp.data.model.auth.RegisterRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPData
import com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPRequest
import com.yash091099.ChiragFarmersApp.data.model.auth.UserDetailsData
import com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPData
import com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressesResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.DefaultLocationResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/msg91/send-otp")
    suspend fun sendLoginOTP(
        @Body request: SendOTPRequest
    ): AuthResponse<SendOTPData>

    @POST("api/msg91/verify-otp")
    suspend fun verifyLoginOTP(
        @Body request: VerifyOTPRequest
    ): AuthResponse<VerifyOTPData>

    @POST("api/msg91/send-registration-otp")
    suspend fun sendRegistrationOTP(
        @Body request: SendOTPRequest
    ): AuthResponse<SendOTPData>

    @POST("api/msg91/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse<VerifyOTPData>

    @GET("api/msg91/get-user-details")
    suspend fun getUserDetails(
        @Header("Authorization") authorization: String
    ): AuthResponse<UserDetailsData>

    @POST("api/msg91/add-business-info")
    suspend fun addBusinessInfo(
        @Header("Authorization") authorization: String,
        @Body request: AddBusinessInfoRequest
    ): AuthResponse<UserDetailsData>

    @GET("api/msg91/check-profile-status")
    suspend fun checkProfileStatus(
        @Header("Authorization") authorization: String
    ): AuthResponse<Boolean>

    @POST("api/farmers/delivery-location")
    suspend fun addDeliveryLocation(
        @Header("Authorization") authorization: String,
        @Body request: AddDeliveryLocationRequest
    ): AddDeliveryLocationResponse

    @POST("api/farmers/update-default-location")
    suspend fun updateDefaultLocation(
        @Header("Authorization") authorization: String,
        @Body request: UpdateDefaultLocationRequest
    ): UpdateDefaultLocationResponse

    @GET("api/farmers/delivery-locations")
    suspend fun getFarmerAddresses(
        @Header("Authorization") authorization: String
    ): FarmerAddressesResponse

    @GET("api/farmers/default-location")
    suspend fun getDefaultLocation(
        @Header("Authorization") authorization: String
    ): DefaultLocationResponse
}

