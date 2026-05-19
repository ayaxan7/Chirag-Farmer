package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductReviewsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: ProductReviewsData? = null,
    @SerializedName("message")
    val message: String
)

data class ProductReviewsData(
    @SerializedName("averageRating")
    val averageRating: Double = 0.0,
    @SerializedName("totalRatings")
    val totalRatings: Int = 0,
    @SerializedName("totalReviews")
    val totalReviews: Int = 0,
    @SerializedName("ratingBreakdown")
    val ratingBreakdown: Map<String, Int> = emptyMap(),
    @SerializedName("recentReviews")
    val recentReviews: List<ProductReviewItem> = emptyList()
)

data class ProductReviewItem(
    @SerializedName("reviewId")
    val reviewId: String? = null,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userProfileImage")
    val userProfileImage: String? = null,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("review")
    val review: String? = null,
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("dislikes")
    val dislikes: Int = 0,
    @SerializedName("recordedAt")
    val recordedAt: String
)

