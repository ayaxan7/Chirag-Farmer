package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.CreateRazorpayOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RazorpayApiService {
    @POST("api/farmers/orders/checkout")
    suspend fun createCheckoutOrder(
        @Header("Authorization") token: String,
        @Body request: PlaceOrderRequest
    ): CreateRazorpayOrderResponse

    @POST("api/farmers/orders/razorpay/verify")
    suspend fun verifyPayment(
        @Header("Authorization") token: String,
        @Body request: VerifyRazorpayPaymentRequest
    ): VerifyRazorpayPaymentResponse

    @GET("api/farmers/orders/razorpay/status/{razorpayOrderId}")
    suspend fun getPaymentStatus(
        @Header("Authorization") token: String,
        @Path("razorpayOrderId") razorpayOrderId: String
    ): VerifyRazorpayPaymentResponse

    @DELETE("api/farmers/orders/razorpay/session/{razorpayOrderId}")
    suspend fun cancelCheckoutSession(
        @Header("Authorization") token: String,
        @Path("razorpayOrderId") razorpayOrderId: String
    ): ResponseBody
}
