package com.ayaan.chiragfarmer.ui.presentation.navigation.navbar

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.ayaan.chiragfarmer.ui.presentation.navigation.destinations.AppDestinations

@PreviewScreenSizes
@Composable
fun ChiragFarmerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                    Icon(
                        if(currentDestination==it) painterResource(id = it.selectedIcon) else painterResource(id = it.unSelectedIcon),
                        contentDescription = it.label
                    )
                },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it })
            }
        }) {

    }
}