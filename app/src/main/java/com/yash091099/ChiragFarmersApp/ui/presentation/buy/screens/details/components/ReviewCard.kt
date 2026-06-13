package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack

@Composable
fun ReviewCard(
    userName: String,
    userImage: Int,
    userImageUrl: String? = null,
    rating: Float,
    reviewText: String,
    timeAgo: String,
    likeCount: Int,
    unLikeCount:Int,
    onUnLikeClick: () -> Unit,
    onLikeClick: () -> Unit,
    isActionLoading: Boolean = false,
) {
    Column {
        // Top section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            // User Image
            if (!userImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = userImageUrl,
                    contentDescription = userName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = painterResource(id = userImage),
                    contentDescription = userName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                // Username + time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BGBlack,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = timeAgo,
                        fontSize = 11.sp
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.review_rating_description),
                            tint = if (index < rating.toInt())
                                Color(0xFFFFC107)
                            else
                                Color(0xFFE0E0E0),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = if (rating % 1f == 0f) {
                            rating.toInt().toString()
                        } else {
                            rating.toString()
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = BGBlack
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Review text aligned from image start
        Text(
            text = reviewText,
            fontSize = 13.sp,
            color = Color(0xFF666666),
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Like/Dislike row aligned from image start
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.size(24.dp),
                enabled = !isActionLoading
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = stringResource(R.string.review_like_description),
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = likeCount.toString(),
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
            IconButton(
                onClick = onUnLikeClick,
                modifier = Modifier.size(24.dp),
                enabled = !isActionLoading
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbDown,
                    contentDescription = stringResource(R.string.review_unlike_description),
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = unLikeCount.toString(),
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }
    }
}