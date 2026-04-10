package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.domain.model.Location

interface LocationRepository {
    suspend fun getPlaceSuggestions(query: String): Result<List<Location>>
}
