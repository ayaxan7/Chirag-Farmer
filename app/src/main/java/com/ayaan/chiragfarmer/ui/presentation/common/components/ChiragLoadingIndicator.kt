package com.ayaan.chiragfarmer.ui.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.ayaan.chiragfarmer.ui.theme.BGBlack

@Composable
fun ChiragLoadingIndicator(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = BGBlack,
            strokeWidth = 3.dp,
            strokeCap = StrokeCap.Round
        )
    }
}