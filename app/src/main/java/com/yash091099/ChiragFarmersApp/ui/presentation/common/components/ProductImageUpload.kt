package com.yash091099.ChiragFarmersApp.ui.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun ProductImageUpload(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(100.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Dashed border background
            Canvas(modifier = Modifier.matchParentSize()) {
                val borderWidth = 2.dp.toPx()
                val dashWidth = 8.dp.toPx()
                val dashGap = 6.dp.toPx()
                val cornerRadius = 8.dp.toPx()

                drawRoundRect(
                    color = Color(0xFFD0D0D0),
                    topLeft = Offset(borderWidth / 2, borderWidth / 2),
                    size = Size(size.width - borderWidth, size.height - borderWidth),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(
                        width = borderWidth, pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(dashWidth, dashGap), 0f
                        )
                    )
                )
            }

            // Plus icon
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.common_add_image_description),
                tint = Color(0xFF888888),
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "Produce Image", fontSize = 11.sp, color = TextGray
        )
    }
}