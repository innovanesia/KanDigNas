package id.innovanesia.kandignas.backend.models

data class UsersData(
    val account_type: String,
    val balance: Int,
    val email: String,
    val fullname: String,
    val nik: String?,
    val nis: String?,
    val nisn: String?,
    val password: String,
    val phone: String,
    val username: String
)