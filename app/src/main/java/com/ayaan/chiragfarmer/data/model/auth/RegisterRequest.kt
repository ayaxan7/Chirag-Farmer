package com.ayaan.chiragfarmer.data.model.auth

data class RegisterRequest(
    val phone: String,
    val otp: String,
    val requestId: String,
    val role: String = "farmer"
)

