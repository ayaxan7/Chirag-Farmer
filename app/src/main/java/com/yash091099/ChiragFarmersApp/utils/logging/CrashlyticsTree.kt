package com.yash091099.ChiragFarmersApp.utils.logging

import android.util.Log as AndroidLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsTree @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val tagStr = tag ?: "Timber"

        if (message.isNotEmpty()) {
            crashlytics.log("[$tagStr] $message")
        }

        if (t != null) {
            crashlytics.recordException(t)
        }

        if (priority == AndroidLog.ERROR && message.isNotEmpty() && t == null) {
            crashlytics.recordException(IllegalStateException("[$tagStr] $message"))
        }
    }
}
