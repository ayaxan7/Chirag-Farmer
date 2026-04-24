package com.yash091099.ChiragFarmersApp.utils

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.dashedBorder(
    width: Dp, color: Color, cornerRadius: Dp
) = composed {
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { width.toPx() }
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }

    drawBehind {
        drawRoundRect(
            color = color, style = Stroke(
                width = strokeWidthPx,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            ), cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
        )
    }
}
