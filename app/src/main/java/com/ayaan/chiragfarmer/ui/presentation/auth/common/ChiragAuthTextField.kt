package com.ayaan.chiragfarmer.ui.presentation.auth.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BorderColour

@Composable
fun ChiragAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxChars: Int? = null
) {
    OutlinedTextField(
        value = value, onValueChange = { newValue ->
        if (maxChars != null) {
            onValueChange(newValue.take(maxChars))
        } else {
            onValueChange(newValue)
        }
    }, placeholder = {
        Text(
            text = placeholder, color = BGBlack
        )
    }, leadingIcon = {
        leadingIcon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }, modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp, color = BorderColour, shape = RoundedCornerShape(8.dp)
            ), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    ), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}