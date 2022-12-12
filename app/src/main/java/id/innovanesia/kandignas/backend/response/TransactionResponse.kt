package id.innovanesia.kandignas.backend.response

import id.innovanesia.kandignas.backend.models.TransactionHistory

data class TransactionResponse(
    val message: String,
    val transactions: List<TransactionHistory>
)
