package id.innovanesia.kandignas.backend.models

import com.google.firebase.Timestamp

data class TransactionHistory(
    val name: String,
    val amount: Int,
    val cash_flow: String,
    val transaction_date: Timestamp
)
