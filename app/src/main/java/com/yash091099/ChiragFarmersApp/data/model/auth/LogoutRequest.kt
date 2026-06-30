package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)
