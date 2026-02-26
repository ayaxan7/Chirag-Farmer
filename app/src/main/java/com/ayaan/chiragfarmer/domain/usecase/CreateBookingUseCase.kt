package com.ayaan.chiragfarmer.domain.usecase

import com.ayaan.chiragfarmer.domain.model.BookingRequest
import com.ayaan.chiragfarmer.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(request: BookingRequest): Result<Unit> {
        return repository.createBooking(request)
    }
}
