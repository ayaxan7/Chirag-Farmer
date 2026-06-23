package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.yash091099.ChiragFarmersApp.domain.model.Product

data class SellerDetailsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: SellerDetailsData?
)

data class SellerDetailsData(
    @SerializedName("seller")
    val seller: SellerInfo,
    @SerializedName("stats")
    val stats: SellerStats,
    @SerializedName("products")
    val products: List<SellerProductDto>,
    @SerializedName("pagination")
    val pagination: SellerPagination
)

data class SellerInfo(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("totalRatings")
    val totalRatings: Int
)

data class SellerStats(
    @SerializedName("totalListings")
    val totalListings: Int,
    @SerializedName("soldOutProducts")
    val soldOutProducts: Int
)

data class SellerProductDto(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("originalPrice")
    val originalPrice: Double,
    @SerializedName("discountedPrice")
    val discountedPrice: Double,
    @SerializedName("rating")
    val rating: Double
)

data class SellerPagination(
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("pages")
    val pages: Int
)

fun SellerProductDto.toDomain(): Product {
    return Product(
        productId = productId,
        productName = name,
        imageUrl = imageUrl ?: "",
        sellerName = sellerName,
        effectivePrice = discountedPrice,
        availableQuantity = 0,
        originalPrice = originalPrice,
        rating = rating.toString()
    )
}
