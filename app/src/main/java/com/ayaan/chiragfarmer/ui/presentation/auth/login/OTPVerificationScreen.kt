package com.ayaan.chiragfarmer.ui.presentation.auth.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.auth.login.components.OTPBox
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    modifier: Modifier = Modifier, navController: NavHostController, phoneNumber: String? = ""
) {
    var otpValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    Log.d("OTPVerificationScreen", "phoneNumber: $phoneNumber")

    SideEffect {
        try {
            focusRequester.requestFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        modifier = modifier, containerColor = BGWhite, topBar = {
            TopAppBar(
                title = {
                    Icon(
                        painter = painterResource(R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp)
                            .size(32.dp)
                            .clickable {
                                navController.popBackStack()
                            },
                        tint = Color.Black
                    )
                })
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background decorative drone image at bottom right
            Image(
                painter = painterResource(R.drawable.left_tilt_drone), // Make sure to add this drawable
                contentDescription = "Drone pattern",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(300.dp),
                alpha = 0.3f
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top white section with back button
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(120.dp)
//                        .background(Color.White)
//                        .statusBarsPadding()
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.ic_back_arrow),
//                        contentDescription = "Back",
//                        modifier = Modifier
//                            .padding(start = 16.dp, top = 16.dp)
//                            .size(32.dp)
//                            .clickable {
//                                navController.popBackStack()
//                            },
//                        tint = Color.Black
//                    )
//                }

                // Main content on black background
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(top = 40.dp)
                ) {
                    // Title
                    Text(
                        text = "Enter authentication code",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = "Enter the 4-digit that we have sent to your registered email id.",
                        fontSize = 16.sp,
                        color = Color(0xFFB0B0B0),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // OTP Input Boxes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(4) { index ->
                            OTPBox(
                                value = otpValue.getOrNull(index)?.toString() ?: "",
                                isFocused = otpValue.length == index,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Hidden text field for capturing input
                    BasicTextField(
                        value = otpValue,
                        onValueChange = { newValue ->
                            if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                otpValue = newValue
                            }
                        },
                        modifier = Modifier
                            .size(0.dp)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Resend code text
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF808080))) {
                            append("Didn't Received code yet? ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.White, fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Resend")
                        }
                    }, fontSize = 14.sp, modifier = Modifier.clickable {
                        // Handle resend OTP
                    })

                    Spacer(modifier = Modifier.weight(1f))

                    // Continue button
                    ChiragButton(
                        text = "Continue",
                        onClick = {
                            // Handle OTP verification
                        },
                        enabled = otpValue.length == 4,
                        modifier = Modifier.padding(bottom = 40.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTPVerificationScreenPreview() {
    OTPVerificationScreen(navController = rememberNavController())
}
