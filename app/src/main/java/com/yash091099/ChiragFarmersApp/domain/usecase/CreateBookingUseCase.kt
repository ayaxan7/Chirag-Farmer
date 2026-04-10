package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.domain.model.BookingRequest
import com.yash091099.ChiragFarmersApp.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(request: BookingRequest): Result<Unit> {
        return repository.createBooking(request)
    }
}
