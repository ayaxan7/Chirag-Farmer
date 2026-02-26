package com.ayaan.chiragfarmer.ui.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.repository.AuthRepository
import com.ayaan.chiragfarmer.domain.model.Location
import com.ayaan.chiragfarmer.domain.usecase.GetLocationSuggestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase
) : ViewModel() {

    private val _isProfileComplete = MutableStateFlow(false)
    val isProfileComplete: StateFlow<Boolean> = _isProfileComplete.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _locationSuggestions = MutableStateFlow<List<Location>>(emptyList())
    val locationSuggestions: StateFlow<List<Location>> = _locationSuggestions.asStateFlow()

    private val _locationQuery = MutableStateFlow("")
    val locationQuery: StateFlow<String> = _locationQuery.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()

    init {
        // Load profile status from DataStore
        viewModelScope.launch {
            authRepository.getProfileStatus().collect { status ->
                _isProfileComplete.value = status
            }
        }
        // Fetch fresh profile status from API
        fetchProfileStatus()
    }

    fun fetchProfileStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.checkProfileStatus()
            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        _isProfileComplete.value = response.data
                    }
                },
                onFailure = {
                    Log.e("HomeViewModel", "Error fetching profile status: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }

    fun onLocationQueryChange(query: String) {
        _locationQuery.value = query
        // Reset selected location when user manually types
        _selectedLocation.value = null
        
        if (query.length < 3) {
            _locationSuggestions.value = emptyList()
            return
        }

        viewModelScope.launch {
            kotlinx.coroutines.delay(500) // Debounce
            if (query == _locationQuery.value) {
                val result = getLocationSuggestionsUseCase(query)
                result.onSuccess { suggestions ->
                    _locationSuggestions.value = suggestions
                }.onFailure {
                    Log.e("HomeViewModel", "Error fetching location suggestions: ${it.message}")
                }
            }
        }
    }

    fun onLocationSelected(location: Location) {
        _locationQuery.value = location.displayName
        _selectedLocation.value = location
        _locationSuggestions.value = emptyList()
        Log.d("HomeViewModel", "Selected location: ${location.displayName} (${location.latitude}, ${location.longitude})")
    }

    fun clearSuggestions() {
        _locationSuggestions.value = emptyList()
    }

    fun clearLocationSelection() {
        _locationQuery.value = ""
        _selectedLocation.value = null
        _locationSuggestions.value = emptyList()
    }

    suspend fun logout() {
        authRepository.logout()
    }
}

