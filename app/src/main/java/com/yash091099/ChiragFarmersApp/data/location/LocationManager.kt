package com.yash091099.ChiragFarmersApp.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationManager @Inject constructor(
   @ApplicationContext private val context: Context
) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val systemLocationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * Check if location permission is granted
     */
    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if location services (GPS) are enabled on the device
     */
    fun isLocationServiceEnabled(): Boolean {
        return systemLocationManager.isLocationEnabled
    }

    /**
     * Get current device location
     * Returns null if:
     * - Permission not granted
     * - Location service not enabled
     * - Location is unavailable
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        // Check permissions
        if (!isLocationPermissionGranted()) {
            return null
        }

        // Check if location service is enabled
        if (!isLocationServiceEnabled()) {
            return null
        }

        return try {
            val location = fusedLocationProviderClient.lastLocation.await()
            if (location != null) {
                Pair(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

