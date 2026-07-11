package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetCartResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CartDataWrapper = CartDataWrapper()
)

data class CartDataWrapper(
    @SerializedName("items")
    val items: List<CartItemDto> = emptyList(),
    @SerializedName("summary")
    val summary: CartSummary = CartSummary(),
    @SerializedName("currentDefaultAddress")
    val currentDefaultAddress: CartAddressDto? = null,
    @SerializedName("walletBalance")
    val walletBalance: Double = 0.0
)

data class CartSummary(
    @SerializedName("subtotal")
    val subtotal: Double = 0.0,
    @SerializedName("totalDiscount")
    val totalDiscount: Double = 0.0,
    @SerializedName("totalDeliveryFee")
    val totalDeliveryFee: Double = 0.0,
    @SerializedName("totalAmount")
    val totalAmount: Double = 0.0
)

data class CartItemDto(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("productName")
    val productName: String?="",
    @SerializedName("productImage")
    val productImage: String? = null,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("finalPrice")
    val finalPrice: Double,
    @SerializedName("quantity")
    val quantity: Int
)

data class CartAddressDto(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("addressString")
    val addressString: String = "",
    @SerializedName("pincode")
    val pincode: String = ""
)

