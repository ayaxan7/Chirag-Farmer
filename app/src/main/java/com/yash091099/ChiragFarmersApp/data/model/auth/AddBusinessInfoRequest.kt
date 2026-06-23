package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class AddBusinessInfoRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("companyName")
    val companyName: String? = null,
    @SerializedName("gstNumber")
    val gstNumber: String? = null,
    @SerializedName("businessAddress")
    val businessAddress: String? = null,
    @SerializedName("pincode")
    val pincode: String? = null,
    @SerializedName("vendorName")
    val vendorName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("companyLogo")
    val companyLogo: String? = null,
    @SerializedName("signature")
    val signature: String? = null,
    @SerializedName("aadhaarFront")
    val aadhaarFront: String? = null,
    @SerializedName("aadhaarBack")
    val aadhaarBack: String? = null,
    @SerializedName("droneImage")
    val droneImage: String? = null,
    @SerializedName("profileImage")
    val profileImage: String? = null,
    @SerializedName("stateName")
    val stateName: LocationItem? = null,
    @SerializedName("townName")
    val townName: LocationItem? = null,
    @SerializedName("region")
    val region: LocationItem? = null
)

