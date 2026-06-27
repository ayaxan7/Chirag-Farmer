package com.yash091099.ChiragFarmersApp.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class PendingPayment(
    val razorpayOrderId: String,
    val amount: Double,
    val paymentType: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = STATUS_PENDING
) {
    companion object {
        const val STATUS_PENDING = "Pending"
        const val STATUS_COMPLETED = "Completed"
        const val STATUS_FAILED = "Failed"
    }
}

@Singleton
class PendingPaymentStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun savePendingPayment(payment: PendingPayment) {
        val payments = getPendingPayments().toMutableList()
        payments.removeAll { it.razorpayOrderId == payment.razorpayOrderId }
        payments.add(payment)
        saveAll(payments)
    }

    fun getPendingPayments(): List<PendingPayment> {
        val json = prefs.getString(KEY_PAYMENTS, null) ?: return emptyList()
        val type = object : TypeToken<List<PendingPayment>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getPendingPaymentsByStatus(status: String): List<PendingPayment> {
        return getPendingPayments().filter { it.status == status }
    }

    fun markCompleted(razorpayOrderId: String) {
        updateStatus(razorpayOrderId, PendingPayment.STATUS_COMPLETED)
    }

    fun markFailed(razorpayOrderId: String) {
        updateStatus(razorpayOrderId, PendingPayment.STATUS_FAILED)
    }

    fun remove(razorpayOrderId: String) {
        val payments = getPendingPayments().toMutableList()
        payments.removeAll { it.razorpayOrderId == razorpayOrderId }
        saveAll(payments)
    }

    fun clearAll() {
        prefs.edit().remove(KEY_PAYMENTS).apply()
    }

    private fun updateStatus(razorpayOrderId: String, status: String) {
        val payments = getPendingPayments().toMutableList()
        val index = payments.indexOfFirst { it.razorpayOrderId == razorpayOrderId }
        if (index != -1) {
            payments[index] = payments[index].copy(status = status)
            saveAll(payments)
        }
    }

    private fun saveAll(payments: List<PendingPayment>) {
        prefs.edit().putString(KEY_PAYMENTS, gson.toJson(payments)).apply()
    }

    private companion object {
        const val PREFS_NAME = "pending_payments"
        const val KEY_PAYMENTS = "payments"
    }
}
