package com.yash091099.ChiragFarmersApp.ui.presentation.splash

import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Dialog to request location permission from the user
 */
@Composable
fun LocationPermissionDialog(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onPermissionDenied() },
        title = { Text("Location Permission Required") },
        text = { Text("This app needs access to your location to provide location-based services. Please grant location permission to continue.") },
        confirmButton = {
            Button(onClick = { onPermissionGranted() }) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            Button(onClick = { onPermissionDenied() }) {
                Text("Deny")
            }
        }
    )
}

/**
 * Dialog shown when user denies location permission and is being prompted again
 */
@Composable
fun LocationPermissionMandatoryDialog(
    onRetry: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text("Location Permission Mandatory") },
        text = { Text("Location permission is mandatory for this app to function properly. Please grant permission to continue using the app.") },
        confirmButton = {
            Button(onClick = { onRetry() }) {
                Text("Try Again")
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Dialog to prompt user to enable location services
 */
@Composable
fun LocationServiceDisabledDialog(
    onGoToSettings: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text("Location Service Disabled") },
        text = { Text("Location services are disabled on your device. Please enable location services to continue. You will be taken to the location settings.") },
        confirmButton = {
            Button(onClick = {
                // Open location settings
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
                onGoToSettings()
            }) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    )
}

