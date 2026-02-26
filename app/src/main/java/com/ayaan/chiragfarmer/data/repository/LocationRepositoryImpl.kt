package com.ayaan.chiragfarmer.data.repository

import com.ayaan.chiragfarmer.data.remote.LocationApi
import com.ayaan.chiragfarmer.data.remote.dto.toDomain
import com.ayaan.chiragfarmer.domain.model.Location
import com.ayaan.chiragfarmer.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: LocationApi
) : LocationRepository {
    override suspend fun getPlaceSuggestions(query: String): Result<List<Location>> {
        return try {
            val response = api.getPlaceSuggestions(query)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
