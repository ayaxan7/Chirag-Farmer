package com.ayaan.chiragfarmer.data.repository

import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.data.remote.BookingApiService
import com.ayaan.chiragfarmer.data.remote.dto.BookingRequestDto
import com.ayaan.chiragfarmer.domain.model.BookingRequest
import com.ayaan.chiragfarmer.domain.repository.BookingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val apiService: BookingApiService,
    private val authDataStore: AuthDataStore
) : BookingRepository {

    override suspend fun createBooking(request: BookingRequest): Result<Unit> {
        return try {
            val token = authDataStore.getAuthToken().first()
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
