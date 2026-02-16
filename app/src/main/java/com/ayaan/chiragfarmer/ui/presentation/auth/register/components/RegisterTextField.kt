package com.ayaan.chiragfarmer.ui.presentation.auth.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayaan.chiragfarmer.ui.theme.BorderColour
import com.ayaan.chiragfarmer.ui.theme.TextGray

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxChars: Int = Int.MAX_VALUE
) {
    BasicTextField(
        value = value,
        onValueChange = { if (it.length <= maxChars) onValueChange(it) },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
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
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                innerTextField()
            }
        }
    )
}