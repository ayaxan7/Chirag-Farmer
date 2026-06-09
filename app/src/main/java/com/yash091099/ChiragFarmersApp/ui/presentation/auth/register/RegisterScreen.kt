package com.yash091099.ChiragFarmersApp.ui.presentation.auth.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragBasicTextField
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun RegisterScreen(
    navController: NavHostController, viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var state by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { profileImageUri = it }
    }

    LaunchedEffect(uiState) {
        when (val stateUi = uiState) {
            is RegisterUiState.Success -> {
                snackbarHostState.showSnackbar(stateUi.message)
                navController.navigate(Route.RegisterSuccess.path) {
                    popUpTo(Route.Auth.path) { inclusive = true }
                }
                viewModel.resetState()
            }

            is RegisterUiState.Error -> {
                snackbarHostState.showSnackbar(stateUi.message)
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
        ChiragTopBar(
            title = "Complete your registration",
            navController = navController,
            icon = R.drawable.ic_back_arrow
        )
    },
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .navigationBarsPadding()
        ) {

            // Background drone watermark
            Image(
                painter = painterResource(R.drawable.right_tilted_drone),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(220.dp)
                    .offset(x = 40.dp, y = (-60).dp)
                    .alpha(0.15f)
            )

            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                // Profile Photo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Profile Photo", fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(contentAlignment = Alignment.BottomEnd) {

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEAEAEA)), contentAlignment = Alignment.Center
                        ) {
                            if (profileImageUri != null) {
                                AsyncImage(
                                    model = profileImageUri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .clickable { imagePickerLauncher.launch(PickVisualMediaRequest()) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                LabelText("Name*")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Enter Your Full Name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText("Mobile Number*")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    placeholder = "Enter Your Contact Number",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText("Email ID*")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Enter Your Email ID",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText("Select Gender*")
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    GenderButton(
                        text = "Male",
                        isSelected = selectedGender == "Male",
                        onClick = { selectedGender = "Male" })
                    GenderButton(
                        text = "Female",
                        isSelected = selectedGender == "Female",
                        onClick = { selectedGender = "Female" })
                }

                Spacer(modifier = Modifier.height(16.dp))

                LabelText("State*")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = state,
                    onValueChange = { state = it },
                    placeholder = "Select or enter location"
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText("City / Town / Village*")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = village,
                    onValueChange = { village = it },
                    placeholder = "Select or enter location"
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText("Region Field")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = region,
                    onValueChange = { region = it },
                    placeholder = "Select or enter location"
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(16.dp))

                ChiragButton(
                    text = "Continue",
                    onClick = {
                        viewModel.addBusinessInfo(
                            context = context,
                            name = name,
                            email = email,
                            vendorName = null,
                            gender = selectedGender,
                            stateName = state,
                            townName = village,
                            region = region,
                            profileImageUri = profileImageUri,
                            aadhaarFrontImageUri = null,
                            aadhaarBackImageUri = null,
                            droneImageUri = null
                        )
                    },
                    enabled = name.isNotEmpty() && mobileNumber.isNotEmpty() && email.isNotEmpty() && selectedGender != null && uiState !is RegisterUiState.Loading
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun LabelText(text: String) {
    Text(
        text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black
    )
}

@Composable
fun GenderButton(
    text: String, isSelected: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(84.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (isSelected) BGBlack else Color.Transparent
            )
            .clickable { onClick() }
            .border(
                1.dp, if (isSelected) BGBlack else BorderGray
            )) {
        Text(
            text = text,
            color = if (isSelected) BGWhite else TextGray,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}