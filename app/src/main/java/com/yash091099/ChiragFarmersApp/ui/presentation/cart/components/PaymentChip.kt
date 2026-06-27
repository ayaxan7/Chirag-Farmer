package com.yash091099.ChiragFarmersApp.ui.presentation.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

@Composable
fun PaymentChip(
    text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp, if (selected) BGBlack else BorderColour
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) BGBlack else Color.White,
            contentColor = if (selected) Color.White else BGBlack
        )
    ) {
        Text(
            text = text, fontSize = 13.sp, maxLines = 1
        )
    }
}