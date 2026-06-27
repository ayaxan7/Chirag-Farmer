package com.yash091099.ChiragFarmersApp.ui.presentation.mainactivity

import android.content.Intent
import android.os.Bundle
import timber.log.Timber
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragFarmerApp
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val deepLinkFlow = MutableSharedFlow<Intent>(replay = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val dataStore = ChiragDataStore(this)
        lifecycleScope.launch {
            val token = dataStore.getAuthToken().first()
            val userRole = dataStore.getUserRole().first()
            val userPhone = dataStore.getUserPhone().first()

            Timber.d("========== AUTH DATA ==========")
            Timber.d("Token: ${token ?: "No token found"}")
            Timber.d("User Role: ${userRole ?: "No role found"}")
            Timber.d("User Phone: ${userPhone ?: "No phone found"}")
            Timber.d("================================")
        }

        intent?.let { lifecycleScope.launch { deepLinkFlow.emit(it) } }
        setContent {
            ChiragFarmerTheme(darkTheme = false) {
                ChiragFarmerApp(
                    navController = rememberNavController(),
                    deepLinkFlow = deepLinkFlow,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        lifecycleScope.launch { deepLinkFlow.emit(intent) }
    }


}
