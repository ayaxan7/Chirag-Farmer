package com.ayaan.chiragfarmer.ui.presentation.auth.common.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.auth.common.components.OTPBox
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    phoneNumber: String? = "",
    requestId: String? = "",
    isSignUp: Boolean = false,
    viewModel: OTPViewModel = hiltViewModel()
) {
    var otpValue by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }

    // Request focus after a delay to ensure the composable is placed
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100) // Small delay to ensure view is laid out
        try {
            focusRequester.requestFocus()
        } catch (e: Exception) {
            Log.e("OTPVerificationScreen", "Error requesting focus: ${e.message}")
        }
    }

    Log.d(
        "OTPVerificationScreen",
        "phoneNumber: $phoneNumber, requestId: $requestId, isSignUp: $isSignUp"
    )

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is OTPUiState.Success -> {
                Log.d("OTPVerificationScreen", "OTP Verification Success!")
                Log.d("OTPVerificationScreen", "isSignUp: $isSignUp")

                // Navigate based on whether it's signup or login
                if (isSignUp) {
                    // Navigate to RegisterScreen for new users
                    Log.d("OTPVerificationScreen", "Navigating to RegisterScreen (Signup)")
                    navController.navigate(Route.Register.path) {
                        popUpTo(Route.Auth.path) { inclusive = true }
                    }
                } else {
                    // Navigate to HomeScreen for existing users
                    Log.d("OTPVerificationScreen", "Navigating to HomeScreen (Login)")
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Auth.path) { inclusive = true }
                    }
                }
                viewModel.resetState()
            }

            is OTPUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_back_arrow
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .navigationBarsPadding()
        ) {
            // Background decorative drone image at bottom right
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.left_tilt_drone),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(300.dp)
                        .offset(x = 50.dp, y = (-100).dp)
                        .alpha(0.9f)
                )
            }

            // Loading indicator
            if (uiState is OTPUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = "Enter the 4-digit OTP that we have sent to your mobile number.",
                        fontSize = 12.sp,
                        color = BGBlack,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // OTP Input Boxes
                    Row(
                        modifier = Modifier.wrapContentWidth(),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hidden text field for capturing input
                    BasicTextField(
                        value = otpValue,
                        onValueChange = { newValue ->
                            if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                otpValue = newValue
                            }
                        },
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth()
                            .height(1.dp)
                            .alpha(0f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )


                    Spacer(modifier = Modifier.height(32.dp))

                    Spacer(modifier = Modifier.weight(1f))

                    // Continue button
                    ChiragButton(
                        text = "Continue", onClick = {
                            if (phoneNumber != null && requestId != null) {
                                viewModel.verifyOTP(phoneNumber, otpValue, requestId, isSignUp)
                            }
                        }, enabled = otpValue.length == 4 && uiState !is OTPUiState.Loading
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
