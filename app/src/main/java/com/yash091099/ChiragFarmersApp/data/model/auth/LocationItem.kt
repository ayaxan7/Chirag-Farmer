package com.yash091099.ChiragFarmersApp.data.model.auth

import com.google.gson.annotations.SerializedName

data class LocationItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null
)
