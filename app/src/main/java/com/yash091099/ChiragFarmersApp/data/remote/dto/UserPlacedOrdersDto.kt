package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserPlacedOrdersResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UserPlacedOrdersData
)

data class UserPlacedOrdersData(
    @SerializedName("orders")
    val orders: List<UserPlacedOrder>,
    @SerializedName("pagination")
    val pagination: OrderPagination
)

data class UserPlacedOrder(
    @SerializedName("orderObjectId")
    val orderObjectId: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("productPrice")
    val productPrice: Double,
    @SerializedName(value = "productId", alternate = ["product", "productObjectId", "product_id", "itemProductId"])
    val productId: String? = null,
    @SerializedName("itemStatus")
    val itemStatus: String? = null
)

data class OrderPagination(
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("pages")
    val pages: Int
)
