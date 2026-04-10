package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.domain.model.Location
import com.yash091099.ChiragFarmersApp.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationSuggestionsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(query: String): Result<List<Location>> {
        if (query.isEmpty()) return Result.success(emptyList())
        return repository.getPlaceSuggestions(query)
    }
}
