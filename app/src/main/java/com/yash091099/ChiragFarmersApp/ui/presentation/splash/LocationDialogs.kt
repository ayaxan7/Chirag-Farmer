package com.yash091099.ChiragFarmersApp.ui.presentation.splash

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yash091099.ChiragFarmersApp.R

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
        title = { Text(stringResource(R.string.location_permission_title)) },
        text = { Text(stringResource(R.string.location_permission_message)) },
        confirmButton = {
            Button(onClick = { onPermissionGranted() }) {
                Text(stringResource(R.string.location_permission_grant))
            }
        },
        dismissButton = {
            Button(onClick = { onPermissionDenied() }) {
                Text(stringResource(R.string.location_permission_deny))
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
        title = { Text(stringResource(R.string.location_mandatory_title)) },
        text = { Text(stringResource(R.string.location_mandatory_message)) },
        confirmButton = {
            Button(onClick = { onRetry() }) {
                Text(stringResource(R.string.location_mandatory_try_again))
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text(stringResource(R.string.location_mandatory_cancel))
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
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text(stringResource(R.string.location_service_disabled_title)) },
        text = { Text(stringResource(R.string.location_service_disabled_message)) },
        confirmButton = {
            Button(onClick = {
                onGoToSettings()
            }) {
                Text(stringResource(R.string.location_go_to_settings))
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text(stringResource(R.string.location_cancel))
            }
        }
    )
}
