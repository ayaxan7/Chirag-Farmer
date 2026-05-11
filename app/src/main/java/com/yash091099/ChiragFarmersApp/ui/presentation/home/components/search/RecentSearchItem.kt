package com.yash091099.ChiragFarmersApp.ui.presentation.home.components.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

@Composable
fun RecentSearchItem(
    imageRes: Int? = null,
    imageUrl: String? = null,
    searchText: String,
    onItemClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick() }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image and Text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = if (!imageUrl.isNullOrBlank()) {
                        rememberAsyncImagePainter(model = imageUrl)
                    } else {
                        painterResource(id = imageRes ?: R.drawable.sell_category_other)
                    },
                    contentDescription = searchText,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = searchText,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }

        }
        HorizontalDivider(
            thickness = 1.dp,
            color = BorderColour
        )
    }
}