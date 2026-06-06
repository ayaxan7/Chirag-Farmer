package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PhonePeCheckoutResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: PhonePeCheckoutData? = null
)

data class PhonePeCheckoutData(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("orderId")
    val orderId: String? = null,
    @SerializedName("merchantOrderId")
    val merchantOrderId: String? = null,
    @SerializedName("phonePeOrderId")
    val phonePeOrderId: String? = null
)

data class PhonePeVerifyRequest(
    @SerializedName("merchantOrderId")
    val merchantOrderId: String,
    @SerializedName("orderId")
    val orderId: String? = null
)

data class PhonePePaymentStatusResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: PhonePePaymentStatusData? = null
)

data class PhonePePaymentStatusData(
    @SerializedName("merchantOrderId")
    val merchantOrderId: String? = null,
    @SerializedName("orderId")
    val orderId: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("paymentStatus")
    val paymentStatus: String? = null,
    @SerializedName("transactionId")
    val transactionId: String? = null
)

