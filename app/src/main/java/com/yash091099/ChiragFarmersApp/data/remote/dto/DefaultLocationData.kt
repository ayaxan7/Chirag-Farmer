package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DefaultLocationData(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("addressString")
    val addressString: String? = null,
    @SerializedName("pincode")
    val pincode: String? = null,
    @SerializedName("coordinates")
    val coordinates: List<Double>? = null
)