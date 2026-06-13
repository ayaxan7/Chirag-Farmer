package com.yash091099.ChiragFarmersApp.ui.presentation.cart.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.DeleteRed
import kotlin.math.roundToInt

@Composable
fun SwipeToRevealItem(
    onDelete: () -> Unit, content: @Composable () -> Unit
) {
    val maxOffsetPx = with(LocalDensity.current) { 80.dp.toPx() }
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 16.dp)
                .background(DeleteRed)
                .clickable { onDelete() }
                .padding(end = 28.dp),
            contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.swipe_delete_description),
                tint = BGWhite,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
            )
        }

        val animatedOffset by animateFloatAsState(targetValue = offsetX)

        Box(modifier = Modifier
            .offset { IntOffset(animatedOffset.roundToInt(), 0) }
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onHorizontalDrag = { _, dragAmount ->
                    offsetX = (offsetX + dragAmount).coerceIn(-maxOffsetPx, 0f)
                }, onDragEnd = {
                    offsetX = if (offsetX < -maxOffsetPx / 2) {
                        -maxOffsetPx
                    } else {
                        0f
                    }
                })
            }) {
            content()
        }
    }
}