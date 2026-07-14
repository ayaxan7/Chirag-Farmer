package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: NotificationsData? = null
)

data class NotificationsData(
    @SerializedName("notifications")
    val notifications: List<NotificationItemDto> = emptyList(),
    @SerializedName("pagination")
    val pagination: NotificationPagination = NotificationPagination()
)

data class NotificationItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("receivedAt")
    val receivedAt: String,
    @SerializedName("tag")
    val tag: String,
    @SerializedName("data")
    val data: NotificationItemDataDto? = null
)

data class NotificationItemDataDto(
    @SerializedName("orderId")
    val orderId: String? = null,
    @SerializedName("bookingId")
    val bookingId: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("url")
    val url: String? = null
)

data class NotificationPagination(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("limit")
    val limit: Int = 20,
    @SerializedName("pages")
    val pages: Int = 1
)

