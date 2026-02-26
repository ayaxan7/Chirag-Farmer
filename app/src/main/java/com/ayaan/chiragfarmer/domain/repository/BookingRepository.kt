package com.ayaan.chiragfarmer.domain.repository

import com.ayaan.chiragfarmer.domain.model.BookingRequest

interface BookingRepository {
    suspend fun createBooking(request: BookingRequest): Result<Unit>
}
