package com.ayaan.chiragfarmer.ui.presentation.home.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Custom shape for the home top bar with an inverted trapezium bottom edge
 */
class TopBarCurvedShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val cutWidth = with(density) { 36.dp.toPx() }
            val cutHeight = with(density) { 36.dp.toPx() }

            // Start at top-left
            moveTo(0f, 0f)

            // Line to top-right
            lineTo(size.width, 0f)

            // Line down the right side to start of the slant
            lineTo(size.width, size.height - cutHeight)

            // Slant down to the right corner of the bottom flat edge
            lineTo(size.width - cutWidth, size.height)

            // Line across the bottom flat edge
            lineTo(cutWidth, size.height)

            // Slant up to the left side
            lineTo(0f, size.height - cutHeight)

            // Path closes implicitly or explicitly
            close()
        }

        return Outline.Generic(path)
    }
}