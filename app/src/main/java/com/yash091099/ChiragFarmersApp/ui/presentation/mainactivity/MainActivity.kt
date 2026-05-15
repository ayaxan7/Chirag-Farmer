package com.yash091099.ChiragFarmersApp.ui.presentation.mainactivity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragFarmerApp
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Request notification permission for Android 13+
    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MainActivity", "Notification permission granted")
        } else {
            Log.w("MainActivity", "Notification permission denied by user")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Request notification permission for Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Log auth token and user data from DataStore
        val dataStore = ChiragDataStore(this)
        lifecycleScope.launch {
            val token = dataStore.getAuthToken().first()
            val userRole = dataStore.getUserRole().first()
            val userPhone = dataStore.getUserPhone().first()

            Log.d("MainActivity", "========== AUTH DATA ==========")
            Log.d("MainActivity", "Token: ${token ?: "No token found"}")
            Log.d("MainActivity", "User Role: ${userRole ?: "No role found"}")
            Log.d("MainActivity", "User Phone: ${userPhone ?: "No phone found"}")
            Log.d("MainActivity", "================================")
        }

        setContent {
            ChiragFarmerTheme(darkTheme = false) {
                ChiragFarmerApp(
                    navController = rememberNavController(),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}