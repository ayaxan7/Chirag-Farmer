package com.yash091099.ChiragFarmersApp.ui.presentation.profile.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar

@Composable
fun WalletScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val balance = 1500
    var withdrawAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = stringResource(R.string.wallet_title),
                icon = R.drawable.ic_arrow,
                onBackClick = { navController.popBackStack() })
        }, containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Balance Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2C2C2C), Color(0xFF1E1E1E)
                            )
                        )
                    )
            ) {
                // Background subtle design patterns
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(150.dp)
                            .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(100.dp))
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.wallet_balance_label),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₹1,500",
                        color = Color.White,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Withdraw Section Title
            Text(
                text = stringResource(R.string.wallet_withdraw_amount_label),
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Custom TextField for inputting digits only
            BasicTextField(
                value = withdrawAmount,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        withdrawAmount = input
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Normal
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(10.dp))
                            .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart
                    ) {
                        if (withdrawAmount.isEmpty()) {
                            Text(
                                text = stringResource(R.string.wallet_top_up_hint),
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Enter / Withdraw Button
            ChiragButton(
                text = stringResource(R.string.wallet_enter_button),
                onClick = {
                    val amount = withdrawAmount.toIntOrNull()
                    if (amount == null || amount <= 0) {
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT)
                            .show()
                    } else if (amount > balance) {
                        Toast.makeText(context, "Amount exceeds balance limit", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "Withdrawal request of ₹$amount submitted successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        withdrawAmount = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}
