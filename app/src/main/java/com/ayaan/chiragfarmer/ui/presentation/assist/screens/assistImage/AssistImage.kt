package com.ayaan.chiragfarmer.ui.presentation.assist.screens.assistImage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.ChiragFarmerTheme

@Composable
fun AssistImage(navController: NavHostController) {
    Scaffold(
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController, title = "Assist Image",icon=R.drawable.ic_arrow
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Upload a Photo of your\nplant",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Share a clear image of your affected plant or leaf so we can diagnose the disease instantly.",
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssistImagePreview() {
    ChiragFarmerTheme {
        AssistImage(navController = rememberNavController())
    }
}