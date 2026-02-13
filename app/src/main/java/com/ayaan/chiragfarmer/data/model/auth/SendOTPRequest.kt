package com.ayaan.chiragfarmer.data.model.auth

data class SendOTPRequest(
    val phone: String,
    val role: String = "farmer"
)

