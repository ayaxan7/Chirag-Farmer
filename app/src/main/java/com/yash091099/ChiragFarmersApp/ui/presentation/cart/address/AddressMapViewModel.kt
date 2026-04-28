package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.osmdroid.util.GeoPoint
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddressMapViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context)

    private val _currentLocation = MutableStateFlow<GeoPoint?>(null)
    val currentLocation: StateFlow<GeoPoint?> = _currentLocation.asStateFlow()

    private val _currentAddress = MutableStateFlow<String>("45 Lake View Colony, Banjara Hills, Hyderabad, Telangana")
    val currentAddress: StateFlow<String> = _currentAddress.asStateFlow()

    private val _isLoadingLocation = MutableStateFlow(false)
    val isLoadingLocation: StateFlow<Boolean> = _isLoadingLocation.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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
                    _errorMessage.value = "Unable to fetch current location"
                }
            } catch (e: SecurityException) {
                _errorMessage.value = "Location permission not granted"
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching location: ${e.message}"
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
                "Location: $latitude, $longitude"
            }
        } catch (e: IOException) {
            "Location: $latitude, $longitude"
        }
    }

    private fun buildAddressString(address: Address): String {
        val addressParts = mutableListOf<String>()

        // Add street address
        address.thoroughfare?.let { addressParts.add(it) }

        // Add locality (city)
        address.locality?.let { addressParts.add(it) }

        // Add admin area (state/province)
        address.adminArea?.let { addressParts.add(it) }

        // Add postal code
        address.postalCode?.let { addressParts.add(it) }

        return if (addressParts.isNotEmpty()) {
            addressParts.joinToString(", ")
        } else {
            address.getAddressLine(0) ?: "Unknown Location"
        }
    }

    fun updateLocationFromCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val geoPoint = GeoPoint(latitude, longitude)
                _currentLocation.value = geoPoint
                val address = getAddressFromCoordinates(latitude, longitude)
                _currentAddress.value = address
            } catch (e: Exception) {
                _errorMessage.value = "Error updating location: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}


