package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.BookingApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.BookingRequestDto
import com.yash091099.ChiragFarmersApp.domain.model.BookingRequest
import com.yash091099.ChiragFarmersApp.domain.repository.BookingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val apiService: BookingApiService,
    private val chiragDataStore: ChiragDataStore
) : BookingRepository {

    override suspend fun createBooking(request: BookingRequest): Result<Unit> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Not authenticated"))
            }

            val requestDto = BookingRequestDto(
                latitude = request.latitude,
                longitude = request.longitude,
                serviceType = request.serviceType,
                farmArea = request.farmArea,
                cropName = request.cropName,
                locationName = request.locationName
            )

            val response = apiService.createBooking("Bearer $token", requestDto)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
