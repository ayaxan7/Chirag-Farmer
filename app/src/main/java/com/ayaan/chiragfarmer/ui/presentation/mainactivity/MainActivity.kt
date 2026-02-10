package com.ayaan.chiragfarmer.ui.presentation.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ayaan.chiragfarmer.ui.presentation.navigation.navhost.AppNavigation
import com.ayaan.chiragfarmer.ui.theme.ChiragFarmerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ChiragFarmerTheme(darkTheme = false) {
                AppNavigation(
                    navController = rememberNavController(),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}