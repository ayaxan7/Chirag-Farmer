package com.yash091099.ChiragFarmersApp.ui.presentation.home

import android.content.Context
import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.BuildConfig
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.location.LocationManager
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.AppVersionApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.AppVersionRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductItem
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.domain.model.Location
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import com.yash091099.ChiragFarmersApp.domain.usecase.GetLocationSuggestionsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateDefaultLocationUseCase
import com.yash091099.ChiragFarmersApp.utils.DeviceIdProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.messaging.FirebaseMessaging
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
        val smartFarmingProducts: List<MixedProductItem>,
        val directFromFarmersProducts: List<MixedProductItem>,
        val seedProducts: List<MixedProductItem>,
        val popularProducts: List<MixedProductItem>
    ) : HomeMixedProductsUiState()
    data class Error(val message: String) : HomeMixedProductsUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val updateDefaultLocationUseCase: UpdateDefaultLocationUseCase,
    private val productRepository: ProductRepository,
    private val locationManager: LocationManager,
    private val firebaseMessaging: FirebaseMessaging,
    private val appVersionApiService: AppVersionApiService,
    private val chiragDataStore: ChiragDataStore
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
                    Timber.e("Error fetching profile status: ${it.message}")
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
                        smartFarmingProducts = mixedProductsData.vendorProducts,
                        directFromFarmersProducts = mixedProductsData.directFromFarmersProducts,
                        seedProducts = mixedProductsData.seedProducts,
                        popularProducts = mixedProductsData.randomProducts
                    )
                },
                onFailure = { exception ->
                    _homeMixedProductsUiState.value = HomeMixedProductsUiState.Error(
                        exception.message ?: context.getString(R.string.error_failed_load_products)
                    )
                }
            )
        }
    }

    fun retryHomeMixedProducts() {
        loadHomeMixedProducts()
    }

    fun updateFcmDeviceTokenOnScreenOpen() {
        viewModelScope.launch {
            try {
                // Get device ID
                val deviceId = DeviceIdProvider.getDeviceId(context)
                Timber.d("Device ID obtained: $deviceId")

                // Verify Firebase is initialized before attempting to get token
                val firebaseToken = try {
                    firebaseMessaging.token.await()
                } catch (e: IllegalStateException) {
                    Timber.e("Firebase not initialized: ${e.message}")
                    return@launch
                } catch (e: Exception) {
                    Timber.e(e, "Failed to get Firebase token: ${e.message}")
                    return@launch
                }

                if (firebaseToken.isBlank()) {
                    Timber.w("FCM token fetched from Firebase was empty")
                    return@launch
                }

                val endpoint = "${BuildConfig.BASE_URL}api/farmers/device-token"
                Timber.d("FCM token fetched from Firebase: $firebaseToken")
                Timber.d("Device ID: $deviceId")
                Timber.d("Sending FCM token to: $endpoint")

                authRepository.updateDeviceToken(firebaseToken, deviceId).fold(
                    onSuccess = { response ->
                        if (response.success) {
                            Timber.d("FCM token synced successfully with backend")
                        } else {
                            Timber.w("Backend returned a non-success response while syncing FCM token: ${response.message ?: "Unknown error"}")
                        }
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "Error syncing FCM token: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch or sync FCM token: ${e.message}")
            }
        }
    }

    fun reportAppVersionOnScreenOpen() {
        viewModelScope.launch {
            try {
                val token = chiragDataStore.getAuthToken().first() ?: return@launch
                Timber.tag("AppVersion").d("$token ${BuildConfig.VERSION_NAME}")
                appVersionApiService.updateAppVersion(
                    authorization = "Bearer $token",
                    AppVersionRequest(appVersion = BuildConfig.VERSION_NAME)
                )
            } catch (e: Exception) {
                Timber.tag("AppVersion").e(e)
            }
        }
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
                    Timber.e("Error fetching location suggestions: ${it.message}")
                }
            }
        }
    }

    fun onLocationSelected(location: Location) {
        _locationQuery.value = location.displayName
        _selectedLocation.value = location
        _locationSuggestions.value = emptyList()
        Timber.d("Selected location: ${location.displayName} (${location.latitude}, ${location.longitude})")
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
            _bookingStatus.value = BookingStatus.Error(context.getString(R.string.home_booking_fill_fields))
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
                    _bookingStatus.value = BookingStatus.Success(context.getString(R.string.success_booking))
                    clearForm()
                    Timber.d("Booking created successfully: $request ")
                },
                onFailure = {
                    _bookingStatus.value = BookingStatus.Error(it.message ?: context.getString(R.string.error_home_booking_failed))
                    Timber.e("Booking failed: ${it.message}")
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

    // Called every time HomeScreen is displayed to update default location based on current GPS coordinates
    fun updateDefaultLocationOnScreenOpen() {
        viewModelScope.launch {
            try {
                // Get user's current GPS location
                fetchCurrentLocationAndUpdateDefault()
            } catch (e: Exception) {
                Timber.e("Exception in updateDefaultLocationOnScreenOpen: ${e.message}")
            }
        }
    }

    private suspend fun fetchCurrentLocationAndUpdateDefault() {
        try {
            // Use LocationManager to get current location
            val currentLocation = locationManager.getCurrentLocation()

            if (currentLocation != null) {
                val (latitude, longitude) = currentLocation
                Timber.d("Current location obtained: $latitude, $longitude")
                updateLocationWithCoordinates(latitude, longitude)
            } else {
                Timber.w("Unable to get current location from device")
                // Location unavailable - don't send any API request
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching current location: ${e.message}")
        }
    }

    private suspend fun updateLocationWithCoordinates(latitude: Double, longitude: Double) {
        try {
            Timber.d("Updating default location with coordinates: $latitude, $longitude")

            // Call the API to update default location
            val result = updateDefaultLocationUseCase(latitude, longitude)
            result.fold(
                onSuccess = { response ->
                    if (response.success) {
                        Timber.d("Default location updated successfully: ${response.message}")
                    } else {
                        Timber.w("Failed to update default location: ${response.message}")
                    }
                },
                onFailure = { exception ->
                    Timber.e("Error updating default location: ${exception.message}")
                }
            )
        } catch (e: Exception) {
            Timber.e("Exception in updateLocationWithCoordinates: ${e.message}")
        }
    }
}

