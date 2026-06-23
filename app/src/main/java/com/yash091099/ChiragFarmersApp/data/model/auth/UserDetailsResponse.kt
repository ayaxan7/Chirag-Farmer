package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class UserDetailsData(
    @SerializedName("id")
    val id: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("companyName")
    val companyName: String? = null,
    @SerializedName("vendorName")
    val vendorName: String? = null,
    @SerializedName("profileImage")
    val profileImage: String? = null,
    @SerializedName("gstNumber")
    val gstNumber: String? = null,
    @SerializedName("businessAddress")
    val businessAddress: String? = null,
    @SerializedName("pinCode")
    val pinCode: String? = null
)

