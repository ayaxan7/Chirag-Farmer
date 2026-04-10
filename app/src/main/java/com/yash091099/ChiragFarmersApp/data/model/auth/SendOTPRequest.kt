package com.yash091099.ChiragFarmersApp.data.model.auth

data class SendOTPRequest(
    val phone: String,
    val role: String = "farmer"
)

