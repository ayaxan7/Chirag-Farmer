package com.ayaan.chiragfarmer.ui.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayaan.chiragfarmer.ui.presentation.sell.data.BuySellCategory
import com.ayaan.chiragfarmer.ui.theme.BackgroundGray
import com.ayaan.chiragfarmer.ui.theme.BorderGreen

@Composable
fun CategoryItem(
    category: BuySellCategory, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clickable { onClick() }
        .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        // Circular Image Container
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    color = BackgroundGray
                )
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = if (selected) BorderGreen else Color.Transparent,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = category.image),
                contentDescription = category.name,
                modifier = Modifier
                    .fillMaxSize(.9f)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Name
        Text(
            text = category.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.MiddleEllipsis
        )
    }
}
