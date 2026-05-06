package com.yash091099.ChiragFarmersApp.data.model.auth

data class FarmerProfileResponse(
    val success: Boolean,
    val data: FarmerProfileData? = null,
    val message: String? = null
)

data class FarmerProfileData(
    val profileImage: String? = null,
    val username: String? = null,
    val phoneNumber: String? = null,
    val userId: String? = null
)

