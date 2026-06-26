package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: OrderDetailsData
)

data class OrderDetailsData(
    @SerializedName("deliveryAddress")
    val deliveryAddress: OrderDeliveryAddress?,
    @SerializedName("orderStatus")
    val orderStatus: String?,
    @SerializedName("statusTimeline")
    val statusTimeline: StatusTimeline?,
    @SerializedName("subtotal")
    val subtotal: Double?,
    @SerializedName("deliveryFee")
    val deliveryFee: Double?,
    @SerializedName("discount")
    val discount: Double?,
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("orderDate")
    val orderDate: String?,
    @SerializedName("transactionId")
    val transactionId: String?,
    @SerializedName("cancellationReason")
    val cancellationReason: String? = null,
    @SerializedName("items")
    val items: List<OrderDetailItem>
)

data class OrderDeliveryAddress(
    @SerializedName("name")
    val name: String?,
    @SerializedName("completeAddress")
    val completeAddress: String?,
    @SerializedName("pincode")
    val pincode: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)

data class StatusTimeline(
    @SerializedName("placed")
    val placed: String?,
    @SerializedName("packed")
    val packed: String?,
    @SerializedName("shipped")
    val shipped: String?,
    @SerializedName("outForDelivery")
    val outForDelivery: String?,
    @SerializedName("delivered")
    val delivered: String?
)

data class OrderDetailItem(
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("sellerName")
    val sellerName: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?,
    @SerializedName("pricePaid")
    val pricePaid: Double?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName(value = "productId", alternate = ["product", "productObjectId", "product_id", "itemProductId"])
    val productId: String? = null,
    @SerializedName("itemStatus")
    val itemStatus: String? = null,
    @SerializedName("cancellationDetails")
    val cancellationDetails: CancellationDetailsDto? = null
)
