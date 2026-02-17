package com.ayaan.chiragfarmer.ui.presentation.auth.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.auth.register.components.RegisterTextField
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.presentation.common.components.ImageUploadBox
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.utils.Base64ImageUtils

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var vendorName by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var profileImageBase64 by remember { mutableStateOf<String?>(null) }

    // Aadhaar card images
    var aadhaarFrontUri by remember { mutableStateOf<Uri?>(null) }
    var aadhaarFrontBase64 by remember { mutableStateOf<String?>(null) }
    var aadhaarBackUri by remember { mutableStateOf<Uri?>(null) }
    var aadhaarBackBase64 by remember { mutableStateOf<String?>(null) }

    // Drone image
    var droneImageUri by remember { mutableStateOf<Uri?>(null) }
    var droneImageBase64 by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Image picker launcher for profile
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
            profileImageBase64 = Base64ImageUtils.encodeUriToBase64(
                context = context,
                uri = it,
                quality = 80,
                includeDataUriPrefix = true
            )
        }
    }

    // Image picker launcher for Aadhaar front
    val aadhaarFrontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            aadhaarFrontUri = it
            aadhaarFrontBase64 = Base64ImageUtils.encodeUriToBase64(
                context = context,
                uri = it,
                quality = 80,
                includeDataUriPrefix = true
            )
        }
    }

    // Image picker launcher for Aadhaar back
    val aadhaarBackPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            aadhaarBackUri = it
            aadhaarBackBase64 = Base64ImageUtils.encodeUriToBase64(
                context = context,
                uri = it,
                quality = 80,
                includeDataUriPrefix = true
            )
        }
    }

    // Image picker launcher for Drone image
    val droneImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            droneImageUri = it
            droneImageBase64 = Base64ImageUtils.encodeUriToBase64(
                context = context,
                uri = it,
                quality = 80,
                includeDataUriPrefix = true
            )
        }
    }

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is RegisterUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                navController.navigate(Route.RegisterSuccess.path) {
                    popUpTo(Route.Auth.path) { inclusive = true }
                }
                viewModel.resetState()
            }
            is RegisterUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .navigationBarsPadding()
        ) {
            // Background drone watermark (bottom right)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.right_tilted_drone),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(200.dp)
                        .offset(x = 50.dp, y = (-100).dp)
                        .alpha(0.8f)
                )
            }

            // Loading indicator
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Profile Photo",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(contentAlignment = Alignment.BottomEnd) {
                        // Avatar circle
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xffe3e6e7)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (profileImageUri != null) {
                                AsyncImage(
                                    model = profileImageUri,
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = Color(0xffaeb4b7)
                                )
                            }
                        }
                        // Camera badge
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color(0xFFDDDDDD), CircleShape)
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Pick photo",
                                modifier = Modifier.fillMaxSize(0.8f),
                                tint = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LabelText(text = "Name *")
                Spacer(modifier = Modifier.height(6.dp))
                RegisterTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Enter Your Full Name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText(text = "Email ID *")
                Spacer(modifier = Modifier.height(6.dp))
                RegisterTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Enter Your Email Id",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelText(text = "Vendor Name (Optional)")
                Spacer(modifier = Modifier.height(6.dp))
                RegisterTextField(
                    value = vendorName,
                    onValueChange = { vendorName = it },
                    placeholder = "Enter Vendor Name"
                )

                Spacer(modifier = Modifier.height(20.dp))

                LabelText(text = "Aadhaar card")
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ImageUploadBox(
                        label = "Front side of Aadhaar card",
                        width = 100.dp,
                        height = 90.dp,
                        imageUri = aadhaarFrontUri,
                        onClick = { aadhaarFrontPickerLauncher.launch("image/*") }
                    )
                    ImageUploadBox(
                        label = "Back side of Aadhaar card",
                        width = 100.dp,
                        height = 90.dp,
                        imageUri = aadhaarBackUri,
                        onClick = { aadhaarBackPickerLauncher.launch("image/*") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LabelText(text = "Upload Image of Drone available")
                Spacer(modifier = Modifier.height(6.dp))
                ImageUploadBox(
                    label = "Add image of Drone",
                    width = 100.dp,
                    height = 90.dp,
                    imageUri = droneImageUri,
                    onClick = { droneImagePickerLauncher.launch("image/*") }
                )

                Spacer(modifier = Modifier.weight(1f))

                ChiragButton(
                    text = "Continue",
                    onClick = {
                        viewModel.addBusinessInfo(
                            name = name,
                            email = email,
                            vendorName = vendorName.ifBlank { null },
                            profileImage = profileImageBase64,
                            aadhaarFrontImage = aadhaarFrontBase64,
                            aadhaarBackImage = aadhaarBackBase64,
                            droneImage = droneImageBase64
                        )
                    },
                    enabled = name.isNotEmpty() && email.isNotEmpty() && uiState !is RegisterUiState.Loading
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun LabelText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Black,
        fontWeight = FontWeight.Normal
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavHostController(LocalContext.current))
}