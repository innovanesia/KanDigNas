package id.innovanesia.kandignas.backend.models

import com.google.firebase.firestore.FieldValue

data class Transactions(
    val name: String,
    val amount: Int,
    val cash_flow: String,
    val transaction_date: FieldValue
)