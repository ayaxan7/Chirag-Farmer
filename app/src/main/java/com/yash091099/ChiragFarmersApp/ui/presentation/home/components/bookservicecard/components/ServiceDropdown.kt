package com.yash091099.ChiragFarmersApp.ui.presentation.home.components.bookservicecard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun ServiceDropdown(
    modifier: Modifier = Modifier,
    selectedService: String,
    placeholder: String = "Select Service",
    onServiceSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val services = listOf(
        "Drone Spraying",
        "Tractor Services",
        "Harvesting Machines",
        "Soil Testing",
        "Power Weeder",
        "Manual Sprayer",
        "Thresher / Winnower",
        "Biofertilizer",
        "Seedling Supply",
        "Irrigation",
        "Custom Equipment (Rental)",
        "Farm Labour",
        "Produce Collection & Transport",
        "Crop Advisory / Insurance"
    )

    Box(modifier = modifier) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .background(
                    color = Color.White, shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp, color = BorderColour, shape = RoundedCornerShape(6.dp)
                )
                .clickable { expanded = !expanded }
                .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedService.ifEmpty { placeholder },
                    fontSize = 14.sp,
                    color = if (selectedService.isEmpty()) TextGray else Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.Black
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .height(200.dp)
                    .background(Color.White),
                properties = androidx.compose.ui.window.PopupProperties(focusable = true)
            ) {
                services.forEachIndexed { index, service ->
                    DropdownMenuItem(
                        text = {
                        Text(
                            text = service,
                            fontSize = 14.sp,
                            color = Color(0xFF4A4A4A),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }, onClick = {
                        onServiceSelected(service)
                        expanded = false
                    }, colors = MenuDefaults.itemColors(
                        textColor = Color.Black
                    ), modifier = Modifier.background(Color(0xFFF8F9FB))
                    )
                    if (index < services.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}