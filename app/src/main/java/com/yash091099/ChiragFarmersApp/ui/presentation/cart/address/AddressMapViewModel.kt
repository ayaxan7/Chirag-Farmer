package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.yash091099.ChiragFarmersApp.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.yash091099.ChiragFarmersApp.domain.model.Location
import com.yash091099.ChiragFarmersApp.domain.usecase.GetDefaultDeliveryLocationUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetLocationSuggestionsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.AddDeliveryLocationUseCase
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.osmdroid.util.GeoPoint
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddressMapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDefaultDeliveryLocationUseCase: GetDefaultDeliveryLocationUseCase,
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase,
    private val addDeliveryLocationUseCase: AddDeliveryLocationUseCase
) : ViewModel() {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context)

    private val _currentLocation = MutableStateFlow<GeoPoint?>(null)
    val currentLocation: StateFlow<GeoPoint?> = _currentLocation.asStateFlow()

    private val _currentAddress = MutableStateFlow(context.getString(R.string.address_fetching_location))
    val currentAddress: StateFlow<String> = _currentAddress.asStateFlow()

    private val _locationQuery = MutableStateFlow("")
    val locationQuery: StateFlow<String> = _locationQuery.asStateFlow()

    private val _locationSuggestions = MutableStateFlow<List<Location>>(emptyList())
    val locationSuggestions: StateFlow<List<Location>> = _locationSuggestions.asStateFlow()

    private val _isLoadingLocation = MutableStateFlow(false)
    val isLoadingLocation: StateFlow<Boolean> = _isLoadingLocation.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _hasDefaultLocation = MutableStateFlow(false)
    val hasDefaultLocation: StateFlow<Boolean> = _hasDefaultLocation.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AddressMapNavigationEvent>()
    val navigationEvent: SharedFlow<AddressMapNavigationEvent> = _navigationEvent.asSharedFlow()

    private var searchJob: Job? = null

    // Form fields
    var receiverName = MutableStateFlow("")
    var receiverContact = MutableStateFlow("")
    var floor = MutableStateFlow("")
    var landmark = MutableStateFlow("")
    var selectedCategory = MutableStateFlow("Home")

    init {
        fetchDefaultDeliveryLocation()
    }

    private fun fetchDefaultDeliveryLocation() {
        viewModelScope.launch {
            getDefaultDeliveryLocationUseCase().fold(
                onSuccess = { locationData ->
                    if (locationData != null) {
                        _hasDefaultLocation.value = true
                        _currentAddress.value = locationData.addressString ?: ""

                        // pre-fill some fields if they exist
                        receiverName.value = locationData.receiverName ?: ""
                        receiverContact.value = locationData.receiverContact ?: ""

                        // Set location from coordinates if available
                        locationData.coordinates?.let { coords ->
                            if (coords.size >= 2) {
                                val geoPoint = GeoPoint(coords[1], coords[0])
                                _currentLocation.value = geoPoint
                            }
                        }
                    } else {
                        // If no default location found, fetch current device location
                        _hasDefaultLocation.value = false
                        fetchCurrentLocation()
                    }
                },
                onFailure = {
                    _hasDefaultLocation.value = false
                    // On failure, also try to fetch current location
                    fetchCurrentLocation()
                }
            )
        }
    }

    fun addDeliveryLocation() {
        val location = _currentLocation.value ?: return
        val addressString = _currentAddress.value
        
        // Extract pincode if possible, or leave empty for user to fill if we had a field
        // For now using empty or a placeholder if geocoder doesn't provide it
        val pincode = "123456" // Default or extracted

        val request = AddDeliveryLocationRequest(
            name = selectedCategory.value,
            receiverName = receiverName.value,
            receiverContact = receiverContact.value,
            addressString = addressString,
            completeAddress = addressString, // Using addressString as completeAddress for now
            pincode = pincode,
            latitude = location.latitude,
            longitude = location.longitude,
            floor = floor.value.takeIf { it.isNotBlank() },
            landmark = landmark.value.takeIf { it.isNotBlank() }
        )

        viewModelScope.launch {
            _isLoadingLocation.value = true
            addDeliveryLocationUseCase(request).fold(
                onSuccess = {
                    _isLoadingLocation.value = false
                    _navigationEvent.emit(AddressMapNavigationEvent.NavigateBackToCart)
                },
                onFailure = {
                    _isLoadingLocation.value = false
                    _errorMessage.value = it.message ?: context.getString(R.string.error_failed_add_location)
                }
            )
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                _isLoadingLocation.value = true
                _errorMessage.value = null

                // Get current location
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).await()

                if (location != null) {
                    val geoPoint = GeoPoint(location.latitude, location.longitude)
                    _currentLocation.value = geoPoint

                    // Get address from coordinates
                    val addressFromCoordinates = getAddressFromCoordinates(
                        location.latitude,
                        location.longitude
                    )
                    _currentAddress.value = addressFromCoordinates
                } else {
                    _errorMessage.value = context.getString(R.string.error_unable_fetch_location)
                }
            } catch (e: SecurityException) {
                _errorMessage.value = context.getString(R.string.error_location_permission_not_granted)
            } catch (e: Exception) {
                _errorMessage.value = context.getString(R.string.error_fetching_location, e.message)
            } finally {
                _isLoadingLocation.value = false
            }
        }
    }

    private fun getAddressFromCoordinates(
        latitude: Double,
        longitude: Double
    ): String {
        return try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                buildAddressString(address)
            } else {
                context.getString(R.string.address_location_format, latitude, longitude)
            }
        } catch (e: IOException) {
            context.getString(R.string.address_location_format, latitude, longitude)
        }
    }

    private fun buildAddressString(address: Address): String {
        val addressLines = (0..address.maxAddressLineIndex).map { address.getAddressLine(it) }
        return addressLines.joinToString(", ")
    }

    fun updateLocationFromCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val geoPoint = GeoPoint(latitude, longitude)
                _currentLocation.value = geoPoint
                val address = getAddressFromCoordinates(latitude, longitude)
                _currentAddress.value = address
            } catch (e: Exception) {
                _errorMessage.value = context.getString(R.string.error_updating_location, e.message)
            }
        }
    }

    fun onLocationQueryChange(query: String) {
        _locationQuery.value = query
        searchJob?.cancel()
        if (query.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(300)
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
        _currentLocation.value = GeoPoint(location.latitude, location.longitude)
        _currentAddress.value = location.displayName
        _locationSuggestions.value = emptyList()
        _locationQuery.value = ""
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

sealed class AddressMapNavigationEvent {
    object NavigateBackToCart : AddressMapNavigationEvent()
}



