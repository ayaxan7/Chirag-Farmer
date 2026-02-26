package com.ayaan.chiragfarmer.data.remote

import com.ayaan.chiragfarmer.data.remote.dto.BookingRequestDto
import com.ayaan.chiragfarmer.data.remote.dto.BookingResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BookingApiService {
    @POST("api/farmers/create-booking")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequestDto
    ): BookingResponseDto
}
