package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.domain.model.BookingRequest

interface BookingRepository {
    suspend fun createBooking(request: BookingRequest): Result<Unit>
}
