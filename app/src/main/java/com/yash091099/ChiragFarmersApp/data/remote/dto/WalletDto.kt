package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WalletAddMoneyRequest(
    @SerializedName("amount") val amount: Double
)

data class WalletAddMoneyResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WalletAddMoneyData? = null
)

data class WalletAddMoneyData(
    @SerializedName("razorpayOrderId") val razorpayOrderId: String,
    @SerializedName("razorpayKeyId") val razorpayKeyId: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("receipt") val receipt: String? = null
)

data class WalletVerifyPaymentRequest(
    @SerializedName("razorpay_order_id") val razorpayOrderId: String,
    @SerializedName("razorpay_payment_id") val razorpayPaymentId: String,
    @SerializedName("razorpay_signature") val razorpaySignature: String
)

data class WalletVerifyPaymentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WalletVerifyPaymentData? = null
)

data class WalletVerifyPaymentData(
    @SerializedName("balance") val balance: Double,
    @SerializedName("transaction") val transaction: WalletTransactionDto? = null
)

data class WalletTransactionDto(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("transactionId") val transactionId: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("source") val source: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("razorpayOrderId") val razorpayOrderId: String? = null,
    @SerializedName("razorpayPaymentId") val razorpayPaymentId: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class WalletBalanceResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WalletBalanceData? = null
)

data class WalletBalanceData(
    @SerializedName("balance") val balance: Double,
    @SerializedName("userType") val userType: String? = null
)
