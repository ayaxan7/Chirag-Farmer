package com.yash091099.ChiragFarmersApp.ui.presentation.profile.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.ProfileUiState
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.ProfileViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.UpdateProfileUiState
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.Teal
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val updateProfileUiState by viewModel.updateProfileUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Local states for editing profile fields
    var nameState by remember { mutableStateOf("") }
    var phoneState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var profileImageState by remember { mutableStateOf<Any?>(null) }
    var selectedProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Initialize values from successful profile load once loaded
    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) {
            val profile = (uiState as ProfileUiState.Success).profile
            profile.username?.let { nameState = it }
            profile.phoneNumber?.let { phoneState = if (it.startsWith("+91")) it else "+91 $it" }
            profile.email?.let { emailState=it }
            profile.profileImage?.let { profileImageState = it }
            selectedProfileImageUri = null
        }
    }

    LaunchedEffect(updateProfileUiState) {
        when (val state = updateProfileUiState) {
            is UpdateProfileUiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateProfileState()
                navController.popBackStack()
            }

            is UpdateProfileUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateProfileState()
            }

            else -> Unit
        }
    }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedProfileImageUri = uri
            profileImageState = uri
            Toast.makeText(context, "Profile photo updated successfully!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Scaffold(
        topBar = {
        ChiragTopBar(
            navController = navController,
            title = "Edit Account",
            icon = R.drawable.ic_arrow,
        )
    }, bottomBar = {
        // Premium Save button to save the updated states
        ChiragButton(
            text = if (updateProfileUiState is UpdateProfileUiState.Loading) "Saving..." else "Save Changes",
            onClick = {
                val profile = (uiState as? ProfileUiState.Success)?.profile ?: return@ChiragButton
                viewModel.updateProfile(
                    context = context,
                    originalProfile = profile,
                    editedName = nameState,
                    editedEmail = emailState,
                    selectedImageUri = selectedProfileImageUri
                )
            },
            enabled = uiState is ProfileUiState.Success && updateProfileUiState !is UpdateProfileUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )
    }, containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile Image Section - Clickable to choose image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    }) {
                AsyncImage(
                    model = profileImageState,
                    contentDescription = "Profile Image",
                    placeholder = painterResource(id = R.drawable.profile_placeholder),
                    error = painterResource(id = R.drawable.profile_placeholder),
                    fallback = painterResource(id = R.drawable.profile_placeholder),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(1.dp, BorderColour, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Inline Editable fields list
            EditFieldItem(
                label = "NAME", value = nameState, onValueChange = { nameState = it })

            HorizontalDivider(color = BorderColour, thickness = 1.dp)
//
//            EditFieldItem(
//                label = "PHONE NUMBER",
//                value = phoneState,
//                onValueChange = { phoneState = it },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
//            )
//
//            HorizontalDivider(color = BorderColour, thickness = 1.dp)
            EditFieldItem(
                label = "EMAIL",
                value = emailState,
                onValueChange = { emailState = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            HorizontalDivider(color = BorderColour, thickness = 1.dp)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun EditFieldItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal
            ),
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(Teal),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
