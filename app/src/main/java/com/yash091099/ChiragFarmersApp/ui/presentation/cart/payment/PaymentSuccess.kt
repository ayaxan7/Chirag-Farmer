package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun PaymentSuccess(
    navController: NavHostController,
) {
    Scaffold(
        containerColor = BGWhite, bottomBar = {
            ChiragButton(
                text = "Continue Shopping",
                onClick = { navController.navigate(Route.Home.path) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, start = 16.dp, end = 16.dp)
            )
        }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.payment_success),
                contentDescription = "Payment Success",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Payment Successful", fontSize = 24.sp, fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Thank You for purchase",
                fontSize = 18.sp,
                color = TextGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}