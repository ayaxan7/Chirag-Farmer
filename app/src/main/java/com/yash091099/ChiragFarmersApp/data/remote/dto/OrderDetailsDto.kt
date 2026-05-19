package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    val success: Boolean,
    val message: String,
    val data: OrderDetailsData
)

data class OrderDetailsData(
    val deliveryAddress: OrderDeliveryAddress?,
    val orderStatus: String?,
    val statusTimeline: StatusTimeline?,
    val subtotal: Double?,
    val deliveryFee: Double?,
    val discount: Double?,
    val paymentMethod: String?,
    val orderDate: String?,
    val transactionId: String?,
    val items: List<OrderDetailItem>
)

data class OrderDeliveryAddress(
    val name: String?,
    val completeAddress: String?,
    val pincode: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class StatusTimeline(
    val placed: String?,
    val packed: String?,
    val shipped: String?,
    val outForDelivery: String?,
    val delivered: String?
)

data class OrderDetailItem(
    val imageUrl: String?,
    val productName: String?,
    val sellerName: String?,
    val orderNumber: String?,
    val pricePaid: Double?,
    val quantity: String?,
    @SerializedName(value = "productId", alternate = ["product", "productObjectId", "product_id", "itemProductId"])
    val productId: String? = null,
    val itemStatus: String? = null,
    val cancellationDetails: CancellationDetailsDto? = null
)
