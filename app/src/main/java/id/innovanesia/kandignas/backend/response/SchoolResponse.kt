package id.innovanesia.kandignas.backend.response

import id.innovanesia.kandignas.backend.models.Schools

data class SchoolResponse(
    val message: String,
    val schools: List<Schools>
)
