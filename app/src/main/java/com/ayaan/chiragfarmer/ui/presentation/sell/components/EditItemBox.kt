package com.ayaan.chiragfarmer.ui.presentation.sell.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.theme.EditGreen

@Composable
fun EditItemBox(
    onEdit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(
                color = EditGreen,
                shape = RoundedCornerShape(4.dp)
            )
    ){
        Image(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = "Delete Item",
            modifier = Modifier.fillMaxSize(.5f)
                .align(Alignment.Center)
        )
    }
}