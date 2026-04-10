package com.yash091099.ChiragFarmersApp.ui.presentation.mainactivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.yash091099.ChiragFarmersApp.data.local.AuthDataStore
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragFarmerApp
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Log auth token and user data from DataStore
        val dataStore = AuthDataStore(this)
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