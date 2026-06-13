package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.PhonePeApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeCheckoutResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePePaymentStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeVerifyRequest
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import com.yash091099.ChiragFarmersApp.domain.repository.PhonePeCheckoutRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PhonePeCheckoutRepositoryImpl @Inject constructor(
    private val api: PhonePeApiService,
    private val chiragDataStore: ChiragDataStore
) : PhonePeCheckoutRepository {

    override suspend fun createCheckout(request: PlaceOrderRequest): Result<PhonePeCheckoutResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.createCheckout("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun verifyPayment(request: PhonePeVerifyRequest): Result<PhonePePaymentStatusResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.verifyPayment("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getPaymentStatus(merchantOrderId: String): Result<PhonePePaymentStatusResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.getPaymentStatus("Bearer $token", merchantOrderId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
}

