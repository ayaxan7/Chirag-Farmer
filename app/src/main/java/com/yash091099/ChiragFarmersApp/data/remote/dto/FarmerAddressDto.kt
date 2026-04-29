package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FarmerAddressesResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: FarmerAddressesDataWrapper? = null
)

data class FarmerAddressesDataWrapper(
    @SerializedName("locations")
    val locations: List<FarmerAddressDto> = emptyList()
)

data class FarmerAddressDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("receiverName")
    val receiverName: String? = null,
    @SerializedName("receiverContact")
    val receiverContact: String? = null,
    @SerializedName("addressString")
    val addressString: String? = null,
    @SerializedName("completeAddress")
    val completeAddress: String? = null,
    @SerializedName("pincode")
    val pincode: String? = null,
    @SerializedName("landmark")
    val landmark: String? = null
)

