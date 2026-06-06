package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeCheckoutResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePePaymentStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeVerifyRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PhonePeApiService {
    @POST("api/farmers/orders/checkout")
    suspend fun createCheckout(
        @Header("Authorization") token: String,
        @Body request: PlaceOrderRequest
    ): PhonePeCheckoutResponse

    @POST("api/farmers/orders/phonepe/verify")
    suspend fun verifyPayment(
        @Header("Authorization") token: String,
        @Body request: PhonePeVerifyRequest
    ): PhonePePaymentStatusResponse

    @GET("api/farmers/orders/phonepe/status/{merchantOrderId}")
    suspend fun getPaymentStatus(
        @Header("Authorization") token: String,
        @Path("merchantOrderId") merchantOrderId: String
    ): PhonePePaymentStatusResponse
}

