package com.ayaan.chiragfarmer.domain.repository

import com.ayaan.chiragfarmer.domain.model.Location

interface LocationRepository {
    suspend fun getPlaceSuggestions(query: String): Result<List<Location>>
}
