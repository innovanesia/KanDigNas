package id.innovanesia.kandignas.backend.response

import id.innovanesia.kandignas.backend.models.Users

data class AccountResponse(
    val message: String,
    val user: Users
)
