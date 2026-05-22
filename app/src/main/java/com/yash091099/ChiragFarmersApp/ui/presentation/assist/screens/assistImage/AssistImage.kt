package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistImage

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme
import com.yash091099.ChiragFarmersApp.utils.dashedBorder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AssistImage(navController: NavHostController) {
    val context = LocalContext.current
    val selectedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val cameraImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val cameraPermissionGranted = rememberSaveable {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher for picking image from gallery
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri.value = uri
            cameraImageUri.value = null
        }
    }

    // Launcher for taking photo with camera
    val takeCameraPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && cameraImageUri.value != null) {
            selectedImageUri.value = cameraImageUri.value
        }
    }

    // Launcher for requesting camera permission
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        cameraPermissionGranted.value = isGranted
        if (isGranted) {
            // Permission granted, now open camera
            val imageFile = createImageFile(context)
            cameraImageUri.value =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    imageFile
                )
            takeCameraPhotoLauncher.launch(cameraImageUri.value!!)
        }
    }


    Scaffold(
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Assist Image",
                icon = R.drawable.ic_arrow
            )
        },
        bottomBar = {
            if(selectedImageUri.value!=null){
                ChiragButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { },
                    text = "Next",
                    containerColor = BGBlack,
                    contentColor = BGWhite,
                    enabled = true
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Upload a Photo of your plant",
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
            Spacer(modifier = Modifier.padding(8.dp))

            // Main image box - shows image if selected, otherwise shows upload placeholder
            if (selectedImageUri.value != null) {
                // Display selected image with border
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .dashedBorder(
                            width = 1.dp,
                            color = Color(0xff3BB69A),
                            cornerRadius = 8.dp
                        )
                ) {
                    AsyncImage(
                        model = selectedImageUri.value,
                        contentDescription = "Selected Plant Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            } else {
                // Show upload placeholder box - clickable to open gallery
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dashedBorder(
                            width = 1.dp,
                            color = Color(0xff3BB69A),
                            cornerRadius = 8.dp
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            // Open image picker when clicked
                            pickImageLauncher.launch("image/*")
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.upload_placeholder),
                            contentDescription = "Upload Image",
                            modifier = Modifier.size(45.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Tap to upload photo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 14.sp,
                            color = BGBlack
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        DividerWithOr()
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(
                            modifier = Modifier
                                .width(100.dp)
                                .height(30.dp),
                            onClick = {
                                // Check if permission is already granted
                                if (cameraPermissionGranted.value) {
                                    // Permission already granted, open camera directly
                                    val imageFile = createImageFile(context)
                                    cameraImageUri.value =
                                        FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            imageFile
                                        )
                                    takeCameraPhotoLauncher.launch(cameraImageUri.value!!)
                                } else {
                                    // Request permission
                                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            colors = ButtonColors(
                                containerColor = BGBlack,
                                contentColor = BGWhite,
                                disabledContainerColor = BGBlack,
                                disabledContentColor = BGWhite
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Open Camera"
                            )
                        }
                    }
                }
            }
        }
    }
}

// Helper function to create image file
private fun createImageFile(context: android.content.Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.cacheDir
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

@Composable
fun DividerWithOr(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        DashedDivider(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Or",
            modifier = Modifier.padding(horizontal = 12.dp),
            fontSize = 14.sp,
            color = BGBlack
        )

        DashedDivider(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier, color: Color = BGBlack
) {
    Canvas(
        modifier = modifier.height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(10f, 10f), 0f
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AssistImagePreview() {
    ChiragFarmerTheme {
        AssistImage(navController = rememberNavController())
    }
}
