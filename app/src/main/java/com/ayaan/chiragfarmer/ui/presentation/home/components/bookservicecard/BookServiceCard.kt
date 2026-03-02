package com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.home.HomeViewModel
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@Composable
fun BookServiceCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(165.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {

            Box (
                modifier = Modifier.fillMaxWidth()
            ){
                // Background Image
                Image(
                    painter = painterResource(R.drawable.book_service_card),
                    contentDescription = "Book Service Card",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )

                // Overlay Text
                Text(
                    text = "Need Farm\nServices?\nBook in Minutes",
                    color = BGWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.padding(16.dp)
                        .align(Alignment.TopStart),
                    lineHeight = 18.sp
                )
                Text(
                    text = "From drone spraying, soil testing\nconnect with verified providers instantly.",
                    color = BGWhite,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}