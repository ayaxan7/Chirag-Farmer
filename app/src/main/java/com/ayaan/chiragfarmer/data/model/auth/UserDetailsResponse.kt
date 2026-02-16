package com.ayaan.chiragfarmer.data.model.auth

data class UserDetailsData(
    val id: String,
    val phone: String,
    val name: String? = null,
    val email: String? = null,
    val companyName: String? = null,
    val vendorName: String? = null,
    val profileImage: String? = null,
    val gstNumber: String? = null,
    val businessAddress: String? = null,
    val pinCode: String? = null
)

