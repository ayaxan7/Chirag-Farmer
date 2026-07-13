package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class CreateRazorpayOrderResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int = 200,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CreateRazorpayOrderData? = null
)

data class CreateRazorpayOrderData(
    @SerializedName("razorpayOrderId")
    val razorpayOrderId: String? = null,
    @SerializedName("razorpayKeyId")
    val razorpayKeyId: String? = null,
    @SerializedName("amount")
    val amount: Double? = null,
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("receipt")
    val receipt: JsonElement? = null,
    @SerializedName("orderId")
    val orderId: String? = null,
    @SerializedName("walletContribution")
    val walletContribution: Double? = null
)

data class VerifyRazorpayPaymentRequest(
    @SerializedName("razorpayOrderId")
    val razorpayOrderId: String,
    @SerializedName("razorpayPaymentId")
    val razorpayPaymentId: String,
    @SerializedName("razorpaySignature")
    val razorpaySignature: String
)

data class VerifyRazorpayPaymentResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: VerifyRazorpayPaymentData? = null
)

data class VerifyRazorpayPaymentData(
    @SerializedName("order")
    val order: OrderDetailsDto? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("balance")
    val balance: Double? = null
)
