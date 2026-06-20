package com.yash091099.ChiragFarmersApp.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class AmountFormatterTest {

    @Test
    fun `whole amount is displayed without decimal places`() {
        assertEquals("125", 125.0.formatAmount())
    }

    @Test
    fun `fractional amount is rounded to two decimal places`() {
        assertEquals("125.46", 125.456.formatAmount())
    }

    @Test
    fun `fractional amount retains two decimal places`() {
        assertEquals("125.50", 125.5.formatAmount())
    }
}
