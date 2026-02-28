package com.ayaan.chiragfarmer.data.model.auth

data class AddBusinessInfoRequest(
    val name: String,
    val emailId: String,
    val companyName: String? = null,
    val gstNumber: String? = null,
    val businessAddress: String? = null,
    val pincode: String? = null,
    val vendorName: String? = null,
    val gender: String? = null,
    val companyLogo: String? = null,
    val signature: String? = null,
    val aadhaarFront: String? = null,
    val aadhaarBack: String? = null,
    val droneImage: String? = null,
    val profileImage: String? = null,
    val stateName: LocationItem? = null,
    val townName: LocationItem? = null,
    val region: LocationItem? = null
)

