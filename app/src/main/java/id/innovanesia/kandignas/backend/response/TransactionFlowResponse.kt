package id.innovanesia.kandignas.backend.response

import id.innovanesia.kandignas.backend.models.Transactions

data class TransactionFlowResponse(
    val message: String,
    val transaction: Transactions
)
