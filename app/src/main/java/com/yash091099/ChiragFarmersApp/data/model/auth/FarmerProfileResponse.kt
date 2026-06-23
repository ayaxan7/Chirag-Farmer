package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class FarmerProfileResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: FarmerProfileData? = null,
    @SerializedName("message")
    val message: String? = null
)

data class FarmerProfileData(
    @SerializedName("profileImage")
    val profileImage: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("email")
    val email: String? = null
)

