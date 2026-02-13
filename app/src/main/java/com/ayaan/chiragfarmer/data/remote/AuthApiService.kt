package com.ayaan.chiragfarmer.data.remote

import com.ayaan.chiragfarmer.data.model.auth.AuthResponse
import com.ayaan.chiragfarmer.data.model.auth.RegisterRequest
import com.ayaan.chiragfarmer.data.model.auth.SendOTPData
import com.ayaan.chiragfarmer.data.model.auth.SendOTPRequest
import com.ayaan.chiragfarmer.data.model.auth.VerifyOTPData
import com.ayaan.chiragfarmer.data.model.auth.VerifyOTPRequest
import retrofit2.http.Body
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
}

