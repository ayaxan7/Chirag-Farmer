package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistImage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme
import com.yash091099.ChiragFarmersApp.utils.dashedBorder

@Composable
fun AssistImage(navController: NavHostController) {
    Scaffold(
        containerColor = BGWhite, topBar = {
            ChiragTopBar(
                navController = navController, title = "Assist Image", icon = R.drawable.ic_arrow
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.Start
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .dashedBorder(width = 1.dp, color = Color(0xff3BB69A), cornerRadius = 8.dp)
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
                        onClick = { },
                        colors = ButtonColors(
                            containerColor = BGBlack,
                            contentColor = BGWhite,
                            disabledContainerColor = BGBlack,
                            disabledContentColor = BGWhite
                        ), contentPadding = PaddingValues(0.dp),
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