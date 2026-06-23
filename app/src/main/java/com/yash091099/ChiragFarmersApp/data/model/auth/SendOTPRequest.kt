package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class SendOTPRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("role")
    val role: String = "farmer"
)

