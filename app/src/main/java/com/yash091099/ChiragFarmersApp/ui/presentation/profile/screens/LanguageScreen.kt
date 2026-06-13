package com.yash091099.ChiragFarmersApp.ui.presentation.profile.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import java.util.Locale

data class LanguageOption(
    val code: String,
    val displayName: String,
    val tag: String
)

private fun setLocale(context: Context, languageTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(Context.LOCALE_SERVICE)?.let { localeManager ->
            val locale = Locale.forLanguageTag(languageTag)
            val localeList = LocaleList(locale)
            @Suppress("DEPRECATION")
            (localeManager as android.app.LocaleManager).applicationLocales = localeList
        }
    } else {
        val locale = Locale(languageTag)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun LanguageScreen(navController: NavHostController) {
    val context = LocalContext.current

    val english = stringResource(R.string.lang_english)
    val hindi = stringResource(R.string.lang_hindi)
    val punjabi = stringResource(R.string.lang_punjabi)
    val telugu = stringResource(R.string.lang_telugu)

    val languages = remember {
        listOf(
            LanguageOption("en", english, "en"),
            LanguageOption("hi", hindi, "hi"),
            LanguageOption("pa", punjabi, "pa"),
            LanguageOption("te", telugu, "te")
        )
    }

    val currentTag = context.resources.configuration.locales[0].toLanguageTag()
    var selectedLanguage by remember { mutableStateOf(currentTag.ifEmpty { "en" }) }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = stringResource(R.string.profile_language),
                icon = R.drawable.ic_arrow,
            )
        }, containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.lang_select_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.lang_select_subtitle),
                fontSize = 14.sp,
                color = TextGray
            )
//            Spacer(modifier = Modifier.height(24.dp))

            languages.forEach { lang ->
                LanguageItem(
                    label = lang.displayName,
                    isSelected = selectedLanguage == lang.tag,
                    onClick = {
                        selectedLanguage = lang.tag
                        setLocale(context, lang.tag)
                    }
                )
                HorizontalDivider(color = BorderColour, thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun LanguageItem(
    label: String, isSelected: Boolean, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black,
                unselectedColor = BorderColour
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
