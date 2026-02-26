package com.ayaan.chiragfarmer.data.remote

import com.ayaan.chiragfarmer.BuildConfig
import com.ayaan.chiragfarmer.data.remote.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LocationApi {
    @Headers("User-Agent: ChiragFarmerApp/1.0")
    @GET("search")
    suspend fun getPlaceSuggestions(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 5,
        @Query("countrycodes") countryCodes: String = "in" // Restrict to India as it's for Indian farmers
    ): List<LocationDto>

    companion object {
        const val BASE_URL = BuildConfig.OSM_NOMINATIM_BASE_URL
    }
}
