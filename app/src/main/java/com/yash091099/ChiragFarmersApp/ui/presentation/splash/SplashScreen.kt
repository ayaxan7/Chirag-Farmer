package com.yash091099.ChiragFarmersApp.ui.presentation.splash

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

    // Handle navigation based on auth check status
    LaunchedEffect(authCheckStatus) {
        when (authCheckStatus) {
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
            AuthCheckStatus.Loading -> {}
        }
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
                painter = painterResource(R.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier.height(100.dp)
            )
//            Text(
//                text = "Chirag Farmer",
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            CircularProgressIndicator(
//                color = Color.Black,
//                strokeWidth = 3.dp
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            Text(
//                text = "Checking authentication...",
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
        }
    }
}

