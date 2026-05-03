package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.core.net.toUri

data class UpiAppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable?
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _installedUpiApps = MutableStateFlow<List<UpiAppInfo>>(emptyList())
    val installedUpiApps: StateFlow<List<UpiAppInfo>> = _installedUpiApps.asStateFlow()

    init {
        fetchInstalledUpiApps()
    }

    private fun fetchInstalledUpiApps() {
        val upiIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("upi://pay?pa=dummy@upi")
        }
        
        val packageManager = context.packageManager
        // Use MATCH_ALL (0x00020000) for more comprehensive results across different Android versions
        val resolveInfoList = packageManager.queryIntentActivities(upiIntent, PackageManager.MATCH_ALL)
        Log.d("PaymentViewModel", "Found ${resolveInfoList.size} UPI apps")
        var upiApps = resolveInfoList.map { resolveInfo ->
            UpiAppInfo(
                name = resolveInfo.loadLabel(packageManager).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                icon = resolveInfo.loadIcon(packageManager)
            )
        }.distinctBy { it.packageName }

        // Fallback: If no apps found via intent, check for common UPI apps by package name
        if (upiApps.isEmpty()) {
            Log.d("PaymentViewModel", "No apps found via intent, trying fallback...")
            val commonUpiPackages = listOf(
                "com.google.android.apps.nbu.paisa.user" to "Google Pay",
                "com.phonepe.app" to "PhonePe",
                "net.one97.paytm" to "Paytm",
                "in.org.npci.upiapp" to "BHIM",
                "com.mobikwik_new" to "Mobikwik",
                "com.freecharge.android" to "Freecharge"
            )

            val fallbackApps = mutableListOf<UpiAppInfo>()
            for ((pkg, name) in commonUpiPackages) {
                try {
                    val appInfo = packageManager.getApplicationInfo(pkg, 0)
                    if (appInfo.enabled) {
                        fallbackApps.add(UpiAppInfo(
                            name = name,
                            packageName = pkg,
                            icon = packageManager.getApplicationIcon(appInfo)
                        ))
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    // App not installed
                }
            }
            upiApps = fallbackApps
        }

        Log.d("PaymentViewModel", "Installed UPI apps: $upiApps")
        _installedUpiApps.value = upiApps
    }
}
