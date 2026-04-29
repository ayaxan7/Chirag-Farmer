package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SetDefaultAddressRequest(
    @SerializedName("addressId")
    val addressId: String
)

data class SetDefaultAddressResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: SetDefaultAddressData? = null
)

data class SetDefaultAddressData(
    @SerializedName("currentDefaultAddressId")
    val currentDefaultAddressId: String,
    @SerializedName("address")
    val address: DefaultLocationData? = null
)
