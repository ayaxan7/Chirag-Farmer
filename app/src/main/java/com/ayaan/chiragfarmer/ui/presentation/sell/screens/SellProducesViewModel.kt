package com.ayaan.chiragfarmer.ui.presentation.sell.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.domain.model.Location
import com.ayaan.chiragfarmer.domain.usecase.GetLocationSuggestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellProducesViewModel @Inject constructor(
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase
) : ViewModel() {

    private val _locationQuery = MutableStateFlow("")
    val locationQuery: StateFlow<String> = _locationQuery.asStateFlow()

    private val _locationSuggestions = MutableStateFlow<List<Location>>(emptyList())
    val locationSuggestions: StateFlow<List<Location>> = _locationSuggestions.asStateFlow()

    private var searchJob: Job? = null

    fun onLocationQueryChange(query: String) {
        _locationQuery.value = query
        
        searchJob?.cancel()
        if (query.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce
                getLocationSuggestionsUseCase(query).onSuccess {
                    _locationSuggestions.value = it
                }.onFailure {
                    _locationSuggestions.value = emptyList()
                }
            }
        } else {
            _locationSuggestions.value = emptyList()
        }
    }

    fun onLocationSelected(location: Location) {
        _locationQuery.value = location.displayName
        _locationSuggestions.value = emptyList()
    }
}
