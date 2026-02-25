package com.ayaan.chiragfarmer.ui.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ayaan.chiragfarmer.ui.theme.BGBlack

@Composable
fun CategoryHeader(category: String, btnText: String = "", onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = category, fontSize = 16.sp, fontWeight = FontWeight.W600, color = BGBlack
        )
        TextButton(
            onClick = { onClick() }) {
            Text(
                text = btnText, fontSize = 12.sp, color = BGBlack, fontWeight = FontWeight.W500
            )
        }
    }
}