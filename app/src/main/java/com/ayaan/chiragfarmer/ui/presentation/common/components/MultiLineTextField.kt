package com.ayaan.chiragfarmer.ui.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayaan.chiragfarmer.ui.theme.BorderColour
import com.ayaan.chiragfarmer.ui.theme.TextGray

@Composable
fun MultiLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Int = 120
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color.Black),
        modifier = modifier
            .fillMaxWidth()
            .height(minHeight.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = BorderColour,
                shape = RoundedCornerShape(6.dp)
            ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 14.sp,
                        color = TextGray
                    )
                }
                innerTextField()
            }
        }
    )
}