package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("role")
    val role: String = "farmer"
)

