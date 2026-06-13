package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateLanguageRequest(
    @SerializedName("language")
    val language: String
)

data class UpdateLanguageResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UpdateLanguageData? = null
)

data class UpdateLanguageData(
    @SerializedName("preferredLanguage")
    val preferredLanguage: String
)
