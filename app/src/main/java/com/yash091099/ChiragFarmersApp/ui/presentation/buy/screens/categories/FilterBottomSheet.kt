package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.categories

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton

data class FilterState(
    val minBudget: String = "",
    val maxBudget: String = "",
    val nearBy: Boolean = false,
    val moreThan3Rating: Boolean = false,
    val sortByLowToHigh: Boolean = false,
    val sortByHighToLow: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    initialState: FilterState = FilterState(),
    onApplyFilters: (FilterState) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var minBudget by remember { mutableStateOf(initialState.minBudget) }
    var maxBudget by remember { mutableStateOf(initialState.maxBudget) }
    var nearBy by remember { mutableStateOf(initialState.nearBy) }
    var moreThan3Rating by remember { mutableStateOf(initialState.moreThan3Rating) }
    var sortByLowToHigh by remember { mutableStateOf(initialState.sortByLowToHigh) }
    var sortByHighToLow by remember { mutableStateOf(initialState.sortByHighToLow) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Filter and Close Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Budget Range Section
            Text(
                text = "Budget Range",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Min",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BudgetInputField(
                        value = minBudget,
                        onValueChange = { minBudget = it },
                        placeholder = "100"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Max",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BudgetInputField(
                        value = maxBudget,
                        onValueChange = { maxBudget = it },
                        placeholder = "100"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            DashedDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // Location Section
            Text(
                text = "Location",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            FilterSwitchRow(
                label = "Near By",
                checked = nearBy,
                onCheckedChange = { nearBy = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            DashedDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // Rating Section
            Text(
                text = "Rating",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            FilterSwitchRow(
                label = "More then 3 rating",
                checked = moreThan3Rating,
                onCheckedChange = { moreThan3Rating = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            DashedDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // Sort By Section
            Text(
                text = "Sort by",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            FilterSwitchRow(
                label = "Low Price - High Price",
                checked = sortByLowToHigh,
                onCheckedChange = {
                    sortByLowToHigh = it
                    if (it) sortByHighToLow = false
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            FilterSwitchRow(
                label = "Hight Price - Low Price",
                checked = sortByHighToLow,
                onCheckedChange = {
                    sortByHighToLow = it
                    if (it) sortByLowToHigh = false
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons: Reset and Apply
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ChiragButton(
                    text = "Reset Filters",
                    onClick = {
                        minBudget = ""
                        maxBudget = ""
                        nearBy = false
                        moreThan3Rating = false
                        sortByLowToHigh = false
                        sortByHighToLow = false
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = Color(0xFFE2E8F0),
                    contentColor = Color.Black
                )

                ChiragButton(
                    text = "Apply Filter",
                    onClick = {
                        onApplyFilters(
                            FilterState(
                                minBudget = minBudget,
                                maxBudget = maxBudget,
                                nearBy = nearBy,
                                moreThan3Rating = moreThan3Rating,
                                sortByLowToHigh = sortByLowToHigh,
                                sortByHighToLow = sortByHighToLow
                            )
                        )
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun BudgetInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color(0xFF737373), fontSize = 14.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F5F9),
            unfocusedContainerColor = Color(0xFFF1F5F9),
            disabledContainerColor = Color(0xFFF1F5F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun FilterSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1E293B),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFCBD5E1),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun DashedDivider(modifier: Modifier = Modifier) {
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
