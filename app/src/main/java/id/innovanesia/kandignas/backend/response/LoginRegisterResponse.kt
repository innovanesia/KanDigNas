package id.innovanesia.kandignas.backend.response

import id.innovanesia.kandignas.backend.models.Users

data class LoginRegisterResponse(
    val message: String,
    val user: Users,
    val access_token: String
)
