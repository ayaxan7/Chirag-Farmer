package com.ayaan.chiragfarmer.ui.presentation.sell.screens.sellcategories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@Composable
fun SellCategoriesScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar ={
            ChiragTopBar(
                title = "Sell Produces",
                navController = navController,
                icon = R.drawable.ic_arrow
            )
        },
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) }
        ) {innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Select a category to sell your produce:")
        }
    }
}