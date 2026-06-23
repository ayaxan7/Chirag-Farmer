package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.seller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderGray
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme

data class SellerFilterState(
    val minBudget: String = "",
    val maxBudget: String = "",
    val sortByLatest: Boolean = false,
    val sortByEarliest: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerFilterBottomSheet(
    onDismissRequest: () -> Unit,
    initialState: SellerFilterState = SellerFilterState(),
    onApplyFilters: (SellerFilterState) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var minBudget by remember { mutableStateOf(initialState.minBudget) }
    var maxBudget by remember { mutableStateOf(initialState.maxBudget) }
    var sortByLatest by remember { mutableStateOf(initialState.sortByLatest) }
    var sortByEarliest by remember { mutableStateOf(initialState.sortByEarliest) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = BGWhite,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }) {
        SellerFilterBottomSheetContent(
            minBudget = minBudget,
            onMinBudgetChange = { minBudget = it },
            maxBudget = maxBudget,
            onMaxBudgetChange = { maxBudget = it },
            sortByLatest = sortByLatest,
            onSortByLatestChange = {
                sortByLatest = it
                if (it) sortByEarliest = false
            },
            sortByEarliest = sortByEarliest,
            onSortByEarliestChange = {
                sortByEarliest = it
                if (it) sortByLatest = false
            },
            onResetFilters = {
                minBudget = ""
                maxBudget = ""
                sortByLatest = false
                sortByEarliest = false
            },
            onApplyFilters = {
                onApplyFilters(
                    SellerFilterState(
                        minBudget,
                        maxBudget,
                        sortByLatest,
                        sortByEarliest
                    )
                )
                onDismissRequest()
            },
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun SellerFilterBottomSheetContent(
    minBudget: String,
    onMinBudgetChange: (String) -> Unit,
    maxBudget: String,
    onMaxBudgetChange: (String) -> Unit,
    sortByLatest: Boolean,
    onSortByLatestChange: (Boolean) -> Unit,
    sortByEarliest: Boolean,
    onSortByEarliestChange: (Boolean) -> Unit,
    onResetFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onDismissRequest: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BGWhite)
    ) {

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.seller_sort_filter_title), fontSize = 18.sp, fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = null, tint = BGBlack
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                stringResource(R.string.seller_budget_range), fontSize = 15.sp, fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        stringResource(R.string.seller_min_price), fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(4.dp))

                    SellerBudgetInputField(
                        value = minBudget, onValueChange = onMinBudgetChange, placeholder = "100"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        stringResource(R.string.seller_max_price), fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(4.dp))

                    SellerBudgetInputField(
                        value = maxBudget, onValueChange = onMaxBudgetChange, placeholder = "1000"
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            SellerDashedDivider()
            Spacer(Modifier.height(12.dp))

            Text(
                stringResource(R.string.seller_sort_by), fontSize = 15.sp, fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            SellerFilterSwitchRow(
                label = stringResource(R.string.seller_sort_latest_to_earliest),
                checked = sortByLatest,
                onCheckedChange = onSortByLatestChange
            )

            Spacer(Modifier.height(8.dp))

            SellerFilterSwitchRow(
                label = stringResource(R.string.seller_sort_earliest_to_latest),
                checked = sortByEarliest,
                onCheckedChange = onSortByEarliestChange
            )

            Spacer(Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BGWhite)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ChiragButton(
                text = stringResource(R.string.seller_reset_filters),
                onClick = onResetFilters,
                modifier = Modifier.weight(1f),
                containerColor = Color(0xFFE2E8F0),
                contentColor = BGBlack
            )

            ChiragButton(
                text = stringResource(R.string.seller_apply_filter), onClick = onApplyFilters, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SellerBudgetInputField(
    value: String, onValueChange: (String) -> Unit, placeholder: String
) {
    OutlinedTextField(
        value = value, onValueChange = {
        if (it.all(Char::isDigit)) {
            onValueChange(it)
        }
    }, placeholder = {
        Text(
            text = placeholder,
            fontSize = 13.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center
        )
    }, singleLine = true, textStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp, color = BGBlack
    ), keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number
    ), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0xFFf4fafb),
        unfocusedContainerColor = Color(0xFFf4fafb),
        focusedBorderColor = BorderGray,
        unfocusedBorderColor = BorderGray,
        cursorColor = BGBlack,
        focusedPlaceholderColor = Color(0xFFf4fafb),
        unfocusedPlaceholderColor = Color(0xFFf4fafb)
    ), modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
    )
}

@Composable
fun SellerFilterSwitchRow(
    label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(
                Color(0xFFf4fafb), RoundedCornerShape(8.dp)
            )
            .border(
                1.dp, BorderGray, RoundedCornerShape(8.dp)
            )
            .clickable {
                onCheckedChange(!checked)
            }
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(
            text = label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BGBlack
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(0.72f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = BGWhite, checkedTrackColor = Color(0xFF1E293B),
                uncheckedThumbColor = BGWhite, uncheckedTrackColor = Color(0xFFCBD5E1),
                checkedBorderColor = Color.Transparent, uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SellerDashedDivider(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        drawLine(
            color = Color(0xFFCBD5E1),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = 2f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SellerFilterBottomSheetPreview() {
    ChiragFarmerTheme {
        SellerFilterBottomSheetContent(
            minBudget = "100",
            onMinBudgetChange = {},
            maxBudget = "500",
            onMaxBudgetChange = {},
            sortByLatest = true,
            onSortByLatestChange = {},
            sortByEarliest = false,
            onSortByEarliestChange = {},
            onResetFilters = {},
            onApplyFilters = {},
            onDismissRequest = {})
    }
}
