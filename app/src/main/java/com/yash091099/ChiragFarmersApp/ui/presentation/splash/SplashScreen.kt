package com.yash091099.ChiragFarmersApp.ui.presentation.splash

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val authCheckStatus by viewModel.authCheckStatus.collectAsStateWithLifecycle()

    var showLocationPermissionDialog by remember { mutableStateOf(false) }
    var showLocationPermissionMandatoryDialog by remember { mutableStateOf(false) }
    var showLocationServiceDisabledDialog by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onNotificationPermissionResult(isGranted)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onLocationPermissionGrantedByUser()
        } else {
            viewModel.onLocationPermissionDeniedByUser()
        }
    }

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.onUserReturnedFromSettings()
    }

    // Handle different auth check statuses
    LaunchedEffect(authCheckStatus) {
        when (authCheckStatus) {
            AuthCheckStatus.RequestNotificationPermission -> {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            AuthCheckStatus.NavigateToHome -> {
                navController.navigate(Route.Home.path) {
                    popUpTo(Route.Splash.path) { inclusive = true }
                }
            }
            AuthCheckStatus.NavigateToAuth -> {
                navController.navigate(Route.Auth.path) {
                    popUpTo(Route.Splash.path) { inclusive = true }
                }
            }
            AuthCheckStatus.ShowLocationPermissionDialog -> {
                showLocationPermissionDialog = true
            }
            AuthCheckStatus.ShowLocationPermissionMandatoryDialog -> {
                showLocationPermissionMandatoryDialog = true
            }
            AuthCheckStatus.ShowLocationServiceDisabledDialog -> {
                showLocationServiceDisabledDialog = true
            }
            else -> {}
        }
    }

    // Show location permission dialog
    if (showLocationPermissionDialog) {
        LocationPermissionDialog(
            onPermissionGranted = {
                showLocationPermissionDialog = false
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            onPermissionDenied = {
                showLocationPermissionDialog = false
                viewModel.onLocationPermissionDeniedByUser()
            }
        )
    }

    // Show mandatory location permission dialog
    if (showLocationPermissionMandatoryDialog) {
        LocationPermissionMandatoryDialog(
            onRetry = {
                showLocationPermissionMandatoryDialog = false
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            onCancel = {
                showLocationPermissionMandatoryDialog = false
                viewModel.onMandatoryPermissionDialogCancelled()
            }
        )
    }

    // Show location service disabled dialog
    if (showLocationServiceDisabledDialog) {
        LocationServiceDisabledDialog(
            onGoToSettings = {
                showLocationServiceDisabledDialog = false
                locationSettingsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onCancel = {
                showLocationServiceDisabledDialog = false
                viewModel.onLocationServiceDialogCancelled()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BGWhite),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo_with_text),
                contentDescription = stringResource(R.string.splash_logo_description),
                modifier = Modifier.height(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
//            CircularProgressIndicator(
//                color = Color.Black,
//                strokeWidth = 3.dp
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            Text(
//                text = when (authCheckStatus) {
//                    AuthCheckStatus.CheckingLocationPermission -> "Checking location permission..."
//                    AuthCheckStatus.CheckingLocationService -> "Checking location service..."
//                    else -> "Checking authentication..."
//                },
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
        }
    }
}
