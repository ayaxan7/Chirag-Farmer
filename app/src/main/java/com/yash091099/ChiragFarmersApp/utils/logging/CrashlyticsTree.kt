package com.yash091099.ChiragFarmersApp.utils.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlvticsTree @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (t != null) {
            crashlytics.recordException(t)
        }

        if (message.isNotEmpty()) {
            crashlytics.log(message)
        }
    }
}
