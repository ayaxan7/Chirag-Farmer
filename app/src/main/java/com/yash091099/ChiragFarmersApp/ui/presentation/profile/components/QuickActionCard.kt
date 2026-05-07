package com.yash091099.ChiragFarmersApp.ui.presentation.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

@Composable
fun QuickActionCard(title: String, icon: Int, modifier: Modifier = Modifier,onClick: () -> Unit={}) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite),
        border = BorderStroke(1.dp, BorderColour),
        onClick = { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp,
                color = Color.Black,
            )
        }
    }
}