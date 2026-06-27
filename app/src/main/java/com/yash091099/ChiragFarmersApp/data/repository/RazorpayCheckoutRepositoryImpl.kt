package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.RazorpayApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.CreateRazorpayOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentResponse
import com.yash091099.ChiragFarmersApp.domain.repository.RazorpayCheckoutRepository
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class RazorpayCheckoutRepositoryImpl @Inject constructor(
    private val api: RazorpayApiService,
    private val chiragDataStore: ChiragDataStore
) : RazorpayCheckoutRepository {

    override suspend fun createCheckoutOrder(request: PlaceOrderRequest): Result<CreateRazorpayOrderResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.createCheckoutOrder("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun verifyPayment(request: VerifyRazorpayPaymentRequest): Result<VerifyRazorpayPaymentResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.verifyPayment("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getPaymentStatus(razorpayOrderId: String): Result<VerifyRazorpayPaymentResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.getPaymentStatus("Bearer $token", razorpayOrderId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun cancelCheckoutSession(razorpayOrderId: String): Result<Unit> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            api.cancelCheckoutSession("Bearer $token", razorpayOrderId)
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to cancel checkout session")
            Result.success(Unit)
        }
    }
}
