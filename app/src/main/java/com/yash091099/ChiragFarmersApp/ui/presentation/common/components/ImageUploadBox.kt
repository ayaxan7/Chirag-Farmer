package com.yash091099.ChiragFarmersApp.ui.presentation.common.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun ImageUploadBox(
    label: String,
    modifier: Modifier = Modifier,
    width: Dp = 130.dp,
    height: Dp = 100.dp,
    imageUri: Uri? = null,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(width = width, height = height)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp, color = BorderColour, shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = label,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF333333)
                )
            }
        }
        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 8.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontWeight = W400
            )
        }
    }
}

@Preview
@Composable
fun ImageUploadBoxPreview() {
    ImageUploadBox(
        label = "Front side of Aadhaar card",
        width = 130.dp,
        height = 100.dp,
        imageUri = null,
        onClick = { /* pick image */ }
    )
}