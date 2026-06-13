package com.yash091099.ChiragFarmersApp.ui.presentation.home.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.TextDarkGray

@Composable
fun SearchBarButton(
    modifier: Modifier = Modifier,onClick: () -> Unit={}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = BGWhite
            )
            .border(
                width = 2.dp,
                color=Color(0xffebebeb)
            )
            .padding(horizontal = 16.dp)
            .clickable{
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(R.string.search_icon_description),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier= Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.search_hint), fontSize = 14.sp, color = TextDarkGray,
            )
            Text(
                text = "Products",
                fontSize = 14.sp,
                color = BGBlack,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Right side: Camera icon
        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = stringResource(R.string.search_camera_description),
            modifier = Modifier.size(20.dp)
        )
    }
}