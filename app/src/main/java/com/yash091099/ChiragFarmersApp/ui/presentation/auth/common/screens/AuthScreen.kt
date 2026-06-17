package com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.ChiragAuthTextField
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var mobileNumber by remember { mutableStateOf("") }
    var isSignUpClicked by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                navController.navigate(
                    Route.OTPVerification.createRoute(
                        phone = mobileNumber,
                        requestId = state.requestId,
                        isSignUp = state.isSignUp
                    )
                )
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .navigationBarsPadding()
        ) {

            // background image
            Image(
                painter = painterResource(R.drawable.tilted),
                contentDescription = stringResource(R.string.auth_tilted_drone_description),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(200.dp)
                    .offset(y = (-100).dp)
                    .alpha(0.3f)
            )

            // Loading indicator
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_with_title),
                    contentDescription = stringResource(R.string.auth_app_logo_description),
                    modifier = Modifier
                        .width(280.dp)
                        .height(80.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))

                // Greeting and title
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.auth_hey_there),
                        fontSize = 16.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if(isSignUpClicked) stringResource(R.string.auth_register_to_continue) else stringResource(R.string.auth_login_to_continue),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                ChiragAuthTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    placeholder = stringResource(R.string.auth_mobile_placeholder),
                    leadingIcon = R.drawable.ic_phone_logo,
                    keyboardType = KeyboardType.Phone,
                    maxChars = 10
                )

                Spacer(modifier = Modifier.weight(1f))

                // Sign up/Login text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BGBlack)) {
                                append(if(isSignUpClicked) stringResource(R.string.auth_already_account) else stringResource(R.string.auth_dont_have_account))
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(if(isSignUpClicked) stringResource(R.string.auth_login_label) else stringResource(R.string.auth_signup_label))
                            }
                        },
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            isSignUpClicked = !isSignUpClicked
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Continue button
                ChiragButton(
                    text = stringResource(R.string.auth_continue),
                    onClick = {
                        viewModel.sendOTP(mobileNumber, isSignUpClicked)
                    },
                    enabled = mobileNumber.length == 10 && uiState !is LoginUiState.Loading
                )
            }
        }
    }
}