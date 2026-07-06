package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletAddMoneyResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletBalanceResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentResponse

interface WalletRepository {
    suspend fun addMoney(amount: Double): Result<WalletAddMoneyResponse>
    suspend fun verifyPayment(request: WalletVerifyPaymentRequest): Result<WalletVerifyPaymentResponse>
    suspend fun getBalance(): Result<WalletBalanceResponse>
}
