package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AppVersionRequest(
    @SerializedName("appVersion")
    val appVersion: String
)
