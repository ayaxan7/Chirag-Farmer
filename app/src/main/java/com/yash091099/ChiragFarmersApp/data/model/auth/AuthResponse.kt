package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T? = null
)

data class SendOTPData(
    @SerializedName("requestId")
    val requestId: String
)

data class VerifyOTPData(
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user")
    val user: User? = null
)

data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("role")
    val role: String
)

