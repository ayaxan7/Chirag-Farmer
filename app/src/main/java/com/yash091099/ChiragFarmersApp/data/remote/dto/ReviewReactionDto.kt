package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewReactionRequest(
    @SerializedName("ratingId")
    val ratingId: String,
    @SerializedName("action")
    val action: String
)

data class ReviewReactionResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ReviewReactionData? = null
)

data class ReviewReactionData(
    @SerializedName("ratingId")
    val ratingId: String? = null,
    @SerializedName("likes")
    val likes: Int? = null,
    @SerializedName("dislikes")
    val dislikes: Int? = null
)

