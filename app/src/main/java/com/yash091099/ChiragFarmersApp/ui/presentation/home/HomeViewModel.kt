package com.yash091099.ChiragFarmersApp.ui.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductItem
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.domain.model.Location
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import com.yash091099.ChiragFarmersApp.domain.usecase.GetLocationSuggestionsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateDefaultLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yash091099.ChiragFarmersApp.domain.model.BookingRequest
import com.yash091099.ChiragFarmersApp.domain.usecase.CreateBookingUseCase

sealed class BookingStatus {
    object Idle : BookingStatus()
    object Loading : BookingStatus()
    data class Success(val message: String) : BookingStatus()
    data class Error(val message: String) : BookingStatus()
}

sealed class HomeMixedProductsUiState {
    object Loading : HomeMixedProductsUiState()
    data class Success(
        val vendorProducts: List<MixedProductItem>,
        val seedProducts: List<MixedProductItem>,
        val popularProducts: List<MixedProductItem>
    ) : HomeMixedProductsUiState()
    data class Error(val message: String) : HomeMixedProductsUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val updateDefaultLocationUseCase: UpdateDefaultLocationUseCase,
    private val productRepository: ProductRepository
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

    private val _selectedService = MutableStateFlow("")
    val selectedService: StateFlow<String> = _selectedService.asStateFlow()

    private val _farmArea = MutableStateFlow("")
    val farmArea: StateFlow<String> = _farmArea.asStateFlow()

    private val _cropName = MutableStateFlow("")
    val cropName: StateFlow<String> = _cropName.asStateFlow()

    private val _bookingStatus = MutableStateFlow<BookingStatus>(BookingStatus.Idle)
    val bookingStatus: StateFlow<BookingStatus> = _bookingStatus.asStateFlow()

    private val _homeMixedProductsUiState = MutableStateFlow<HomeMixedProductsUiState>(HomeMixedProductsUiState.Loading)
    val homeMixedProductsUiState: StateFlow<HomeMixedProductsUiState> = _homeMixedProductsUiState.asStateFlow()

    init {
        // Load profile status from DataStore
        viewModelScope.launch {
            authRepository.getProfileStatus().collect { status ->
                _isProfileComplete.value = status
            }
        }
        // Fetch fresh profile status from API
        fetchProfileStatus()
        // Load homescreen mixed products
        loadHomeMixedProducts()
        // Update default location on app launch (only if not already updated in this session)
        updateDefaultLocationOnAppLaunch()
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

    private fun loadHomeMixedProducts() {
        viewModelScope.launch {
            _homeMixedProductsUiState.value = HomeMixedProductsUiState.Loading
            productRepository.getMixedProductsForHomeScreen().fold(
                onSuccess = { mixedProductsData ->
                    _homeMixedProductsUiState.value = HomeMixedProductsUiState.Success(
                        vendorProducts = mixedProductsData.vendorProducts,
                        seedProducts = mixedProductsData.seedProducts,
                        popularProducts = mixedProductsData.randomProducts
                    )
                },
                onFailure = { exception ->
                    _homeMixedProductsUiState.value = HomeMixedProductsUiState.Error(
                        exception.message ?: "Failed to load products"
                    )
                }
            )
        }
    }

    fun retryHomeMixedProducts() {
        loadHomeMixedProducts()
    }

    // ...existing code...

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

    fun onServiceSelected(service: String) {
        _selectedService.value = service
    }

    fun onFarmAreaChange(area: String) {
        _farmArea.value = area
    }

    fun onCropNameChange(crop: String) {
        _cropName.value = crop
    }

    fun createBooking() {
        val location = _selectedLocation.value
        val lat = location?.latitude ?: 0.0
        val lon = location?.longitude ?: 0.0
        val service = _selectedService.value
        val area = _farmArea.value.toIntOrNull() ?: 0
        val crop = _cropName.value
        val locName = _locationQuery.value

        if (lat == 0.0 || lon == 0.0 || service.isEmpty() || area == 0) {
            _bookingStatus.value = BookingStatus.Error("Please fill all required fields correctly")
            return
        }

        viewModelScope.launch {
            _bookingStatus.value = BookingStatus.Loading
            val request = BookingRequest(
                latitude = lat,
                longitude = lon,
                serviceType = service,
                farmArea = area,
                cropName = crop,
                locationName = locName
            )
            val result = createBookingUseCase(request)
            result.fold(
                onSuccess = {
                    _bookingStatus.value = BookingStatus.Success("Booking created successfully")
                    clearForm()
                    Log.d("HomeViewModel", "Booking created successfully: $request ")
                },
                onFailure = {
                    _bookingStatus.value = BookingStatus.Error(it.message ?: "Booking failed")
                    Log.e("HomeViewModel", "Booking failed: ${it.message}")
                }
            )
        }
    }

    private fun clearForm() {
        _selectedService.value = ""
        _farmArea.value = ""
        _cropName.value = ""
        clearLocationSelection()
    }

    fun clearSuggestions() {
        _locationSuggestions.value = emptyList()
    }

    fun clearLocationSelection() {
        _locationQuery.value = ""
        _selectedLocation.value = null
        _locationSuggestions.value = emptyList()
    }

    fun resetBookingStatus() {
        _bookingStatus.value = BookingStatus.Idle
    }

    suspend fun logout() {
        authRepository.logout()
    }

    private fun updateDefaultLocationOnAppLaunch() {
        viewModelScope.launch {
            try {
                // Check if location was already updated on this app launch session
                authRepository.getLocationUpdatedOnLaunch().collect { wasUpdated ->
                    if (!wasUpdated) {
                        // Get user's current GPS location
                        // Note: For production, you would integrate with Location Services
                        // For now, using default coordinates (New Delhi) as a fallback
                        val latitude = 28.6139
                        val longitude = 77.2090

                        Log.d("HomeViewModel", "Updating default location on app launch with coordinates: $latitude, $longitude")

                        // Call the API to update default location
                        val result = updateDefaultLocationUseCase(latitude, longitude)
                        result.fold(
                            onSuccess = { response ->
                                if (response.success) {
                                    Log.d("HomeViewModel", "Default location updated successfully: ${response.message}")
                                } else {
                                    Log.w("HomeViewModel", "Failed to update default location: ${response.message}")
                                }
                            },
                            onFailure = { exception ->
                                Log.e("HomeViewModel", "Error updating default location: ${exception.message}")
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception in updateDefaultLocationOnAppLaunch: ${e.message}")
            }
        }
    }
}

