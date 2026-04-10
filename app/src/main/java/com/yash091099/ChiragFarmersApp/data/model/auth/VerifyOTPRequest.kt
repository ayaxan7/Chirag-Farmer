package com.yash091099.ChiragFarmersApp.data.model.auth

data class VerifyOTPRequest(
    val phone: String,
    val otp: String,
    val requestId: String,
    val role: String = "farmer"
)

