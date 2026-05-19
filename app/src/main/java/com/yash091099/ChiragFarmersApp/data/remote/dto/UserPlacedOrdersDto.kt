package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserPlacedOrdersResponse(
    val success: Boolean,
    val message: String,
    val data: UserPlacedOrdersData
)

data class UserPlacedOrdersData(
    val orders: List<UserPlacedOrder>,
    val pagination: OrderPagination
)

data class UserPlacedOrder(
    val orderObjectId: String,
    val imageUrl: String?,
    val productName: String,
    val sellerName: String,
    val productPrice: Double,
    @SerializedName(value = "productId", alternate = ["product", "productObjectId", "product_id", "itemProductId"])
    val productId: String? = null,
    val itemStatus: String? = null
)

data class OrderPagination(
    val total: Int,
    val page: Int,
    val limit: Int,
    val pages: Int
)
