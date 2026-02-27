package com.ayaan.chiragfarmer.ui.presentation.bookings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@Composable
fun BookingsScreen(navController: NavHostController) {
    val snackbarHostState= remember { SnackbarHostState() }
    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow,
                title="Bookings"
            )
        }
    ) {paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Assist Screen",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

