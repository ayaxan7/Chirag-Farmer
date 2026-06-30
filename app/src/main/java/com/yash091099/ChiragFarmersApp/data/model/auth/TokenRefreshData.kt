package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class TokenRefreshData(
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
