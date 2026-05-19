package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDetailedResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ProductDetailedData?
)

data class ProductDetailedData(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("productImage")
    val productImage: String? = null,
    @SerializedName("productImages")
    val productImages: List<String>,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productDescription")
    val productDescription: String?,
    @SerializedName("keyFeatures")
    val keyFeatures: List<String>,
    @SerializedName("originalPrice")
    val originalPrice: Double,
    @SerializedName("discountedPrice")
    val discountedPrice: Double,
    @SerializedName("discountPercent")
    val discountPercent: Double,
    @SerializedName("seller")
    val seller: SellerProfileInfo,
    @SerializedName("availabilityStatus")
    val availabilityStatus: String,
    @SerializedName("unit")
    val unit: String? = null,
    @SerializedName("quantity")
    val quantity: Double? = null,
    @SerializedName("deliveryFee")
    val deliveryFee: Double? = null,
    @SerializedName("category")
    val category: String,
    @SerializedName("subcategory")
    val subcategory: String? = null,
    @SerializedName("isInCart")
    val isInCart: Boolean = false,
    @SerializedName("similarProducts")
    val similarProducts: List<RecommendedProduct> = emptyList(),
    @SerializedName("moreProducts")
    val moreProducts: List<RecommendedProduct> = emptyList()
)

data class RecommendedProduct(
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
    val discountedPrice: Double
)

data class SellerProfileInfo(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profilePhoto")
    val profilePhoto: String? = null,
    @SerializedName("sellerRating")
    val sellerRating: Double? = null
)

