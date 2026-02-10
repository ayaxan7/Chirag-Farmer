package com.ayaan.chiragfarmer.ui.presentation.auth.login

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragTextField
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.TextGray
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
//    viewModel: LoginViewModel = hiltViewModel()
) {
    var mobileNumber by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BGWhite
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
                contentDescription = "Tilted drone",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(200.dp)
                    .padding(bottom = 48.dp)
                    .alpha(0.3f)
            )

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
                    contentDescription = "App Logo",
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
                        text = "Hey there,",
                        fontSize = 16.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Login to continue",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                ChiragTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    placeholder = "Your Mobile Number",
                    leadingIcon = R.drawable.ic_phone_logo,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.weight(1f))

                // Sign up text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color(0xFF666666))) {
                                append("Don't have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Signup")
                            }
                        },
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
//                            navController.navigate(Route.OTPVerification.path)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Continue button
                ChiragButton(
                    text = "Continue",
                    onClick = {
                        navController.navigate(
                            Route.OTPVerification.createRoute(mobileNumber)
                        )
                    },
//                    enabled = mobileNumber.isNotEmpty()
                )
            }
        }
    }
}