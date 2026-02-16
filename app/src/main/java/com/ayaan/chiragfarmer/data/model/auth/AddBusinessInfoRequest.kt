package com.ayaan.chiragfarmer.data.model.auth

data class AddBusinessInfoRequest(
    val name: String,
    val emailId: String,
    val companyName: String? = null,
    val gstNumber: String? = null,
    val businessAddress: String? = null,
    val pinCode: String? = null,
    val vendorName: String? = null,
    val profileImage: String? = null,
    val aadhaarFrontImage: String? = null,
    val aadhaarBackImage: String? = null,
    val droneImage: String? = null
)

