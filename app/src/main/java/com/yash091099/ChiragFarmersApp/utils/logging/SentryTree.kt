package com.yash091099.ChiragFarmersApp.utils.logging

import android.util.Log as AndroidLog
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import timber.log.Timber

class SentryTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val sentryLevel = when (priority) {
            AndroidLog.DEBUG -> SentryLevel.DEBUG
            AndroidLog.INFO -> SentryLevel.INFO
            AndroidLog.WARN -> SentryLevel.WARNING
            AndroidLog.ERROR -> SentryLevel.ERROR
            AndroidLog.ASSERT -> SentryLevel.FATAL
            else -> SentryLevel.DEBUG
        }

        val breadcrumb = Breadcrumb()
        breadcrumb.level = sentryLevel
        breadcrumb.category = tag ?: "Timber"
        breadcrumb.message = message
        Sentry.addBreadcrumb(breadcrumb)

        if (t != null) {
            Sentry.captureException(t)
        }

        if (priority == AndroidLog.ERROR && message.isNotEmpty() && t == null) {
            Sentry.captureMessage("[$tag] $message", SentryLevel.ERROR)
        }
    }
}
