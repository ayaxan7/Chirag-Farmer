package com.ayaan.chiragfarmer.domain.usecase

import com.ayaan.chiragfarmer.domain.model.Location
import com.ayaan.chiragfarmer.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationSuggestionsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(query: String): Result<List<Location>> {
        if (query.length < 3) return Result.success(emptyList())
        return repository.getPlaceSuggestions(query)
    }
}
