package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AddDeliveryLocationRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("receiverName")
    val receiverName: String,
    @SerializedName("receiverContact")
    val receiverContact: String,
    @SerializedName("addressString")
    val addressString: String,
    @SerializedName("completeAddress")
    val completeAddress: String,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("floor")
    val floor: String? = null,
    @SerializedName("landmark")
    val landmark: String? = null
)

data class AddDeliveryLocationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<DeliveryLocationData> = emptyList()
)

data class DeliveryLocationData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("receiverName")
    val receiverName: String,
    @SerializedName("receiverContact")
    val receiverContact: String,
    @SerializedName("addressString")
    val addressString: String,
    @SerializedName("completeAddress")
    val completeAddress: String,
    @SerializedName("floor")
    val floor: String? = null,
    @SerializedName("landmark")
    val landmark: String? = null,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("coordinates")
    val coordinates: List<Double> = emptyList()
)
