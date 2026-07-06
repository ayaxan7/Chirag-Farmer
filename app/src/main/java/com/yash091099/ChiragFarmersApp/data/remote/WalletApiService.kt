package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletAddMoneyRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletAddMoneyResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletBalanceResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletApiService {
    @POST("api/wallet/buyer/add-money")
    suspend fun addMoney(
        @Header("Authorization") token: String,
        @Body request: WalletAddMoneyRequest
    ): WalletAddMoneyResponse

    @POST("api/wallet/buyer/verify-payment")
    suspend fun verifyPayment(
        @Header("Authorization") token: String,
        @Body request: WalletVerifyPaymentRequest
    ): WalletVerifyPaymentResponse

    @GET("api/wallet/buyer/balance")
    suspend fun getBalance(
        @Header("Authorization") token: String
    ): WalletBalanceResponse
}
