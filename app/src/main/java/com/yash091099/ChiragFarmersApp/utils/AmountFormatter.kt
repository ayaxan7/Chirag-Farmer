package com.yash091099.ChiragFarmersApp.utils

import java.util.Locale

fun Double.formatAmount(): String =
    if (this % 1.0 == 0.0) {
        toLong().toString()
    } else {
        String.format(Locale.US, "%.2f", this)
    }
