package com.ayaan.chiragfarmer.ui.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.sell.components.DeleteItemBox
import com.ayaan.chiragfarmer.ui.presentation.sell.components.EditItemBox
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.BorderGray
import com.ayaan.chiragfarmer.ui.theme.DisabledButtonGray
import com.ayaan.chiragfarmer.ui.theme.TextDarkGray

@Composable
fun CommonProductCard(
    modifier: Modifier = Modifier,
    imageRes: Int? = null,
    imageUrl: String? = null,
    productName: String,
    brandName: String,
    currentPrice: String,
    originalPrice: String? = null,
    rating: String,
    selectedSize: String = "1 Unit",
    onSizeClick: () -> Unit = {},
    isMarkAsSoldRowVisible: Boolean = false,
    onMarkAsSoldClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    isSoldOut: Boolean = false
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .wrapContentHeight(), colors = CardDefaults.cardColors(
            containerColor = BGWhite
        ), border = BorderStroke(
            width = 1.dp, color = BorderGray
        ), shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Product Image
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = productName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(R.drawable.sprayer),
                    error = painterResource(R.drawable.sprayer)
                )
            } else if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = productName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                // Product Name
                Text(
                    text = productName,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Brand Name
                Text(
                    text = brandName, fontSize = 10.sp, fontWeight = W400, color = TextDarkGray
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Price and Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "₹$currentPrice",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        if (originalPrice != null) {
                            Text(
                                text = originalPrice,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextDarkGray,
                                style = TextStyle(
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }
                    }

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_rating_star),
                            contentDescription = "Rating",
                            tint = Color(0xffF7E62D),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = rating, fontSize = 10.sp, fontWeight = W400, color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Size Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Size", fontSize = 11.sp, fontWeight = W500, color = Color.Black
                    )

                    // Size Dropdown
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(24.dp)
                            .background(
                                color = Color.White, shape = RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = 1.dp, color = BorderGray, shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 10.dp), contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedSize, fontSize = 11.sp, color = Color.Black
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Select size",
                                tint = BorderGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                if (isMarkAsSoldRowVisible) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val backgroundColor = if (isSoldOut) DisabledButtonGray else BGBlack
                        val text = if (isSoldOut) "Marked as Sold" else "Mark as Sold"
                        val clickableModifier =
                            if (isSoldOut) Modifier else Modifier.clickable { onMarkAsSoldClick() }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(6.dp))
                                .background(backgroundColor)
                                .then(clickableModifier)
                                .padding(start = 4.dp, top = 2.dp, bottom = 2.dp, end = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Cancel,
                                    contentDescription = text,
                                    modifier = Modifier.size(12.dp),
                                    tint = BGWhite
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    fontWeight = W400,
                                    color = BGWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }
                        if (!isSoldOut) {
                            Spacer(modifier = Modifier.width(8.dp))
                            DeleteItemBox(onDelete = onDeleteClick)
                            Spacer(modifier = Modifier.width(4.dp))
                            EditItemBox(onEdit = onEditClick)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}