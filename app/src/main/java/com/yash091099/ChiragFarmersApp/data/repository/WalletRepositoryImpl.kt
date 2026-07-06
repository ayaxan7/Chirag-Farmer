package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.WalletApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletAddMoneyRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletAddMoneyResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletBalanceResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentResponse
import com.yash091099.ChiragFarmersApp.domain.repository.WalletRepository
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val api: WalletApiService,
    private val chiragDataStore: ChiragDataStore
) : WalletRepository {

    override suspend fun addMoney(amount: Double): Result<WalletAddMoneyResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.addMoney("Bearer $token", WalletAddMoneyRequest(amount))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun verifyPayment(request: WalletVerifyPaymentRequest): Result<WalletVerifyPaymentResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.verifyPayment("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getBalance(): Result<WalletBalanceResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.getBalance("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
}
