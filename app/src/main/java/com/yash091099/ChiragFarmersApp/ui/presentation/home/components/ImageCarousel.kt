package com.yash091099.ChiragFarmersApp.ui.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.LightGray
import kotlinx.coroutines.delay

@Composable
fun ImageCarousel(
    images: List<Int>, modifier: Modifier = Modifier, autoScrollDuration: Long = 3000L,isIndicatorVisible: Boolean=true
) {
    val pagerState = rememberPagerState(
        initialPage = 0, pageCount = { images.size })

    // Auto-scroll
    LaunchedEffect(Unit) {
        while (true) {
            delay(autoScrollDuration)
            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Image Pager
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxWidth()
        ) { page ->
            Image(
                painter = painterResource(images[page]),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    if(isIndicatorVisible){
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        repeat(images.size) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = 8.dp, height = 8.dp
                    )
                    .clip(CircleShape)
                    .background(
                        if (index == pagerState.currentPage) BGBlack
                        else LightGray
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
        }
    Spacer(modifier = Modifier.height(8.dp))
}