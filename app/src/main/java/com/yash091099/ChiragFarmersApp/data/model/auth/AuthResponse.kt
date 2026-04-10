package com.yash091099.ChiragFarmersApp.data.model.auth

data class AuthResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

data class SendOTPData(
    val requestId: String
)

data class VerifyOTPData(
    val verified: Boolean,
    val token: String? = null,
    val user: User? = null
)

data class User(
    val id: String,
    val phone: String,
    val role: String
)

