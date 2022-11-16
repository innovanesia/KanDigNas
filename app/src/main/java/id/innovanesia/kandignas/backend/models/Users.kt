package id.innovanesia.kandignas.backend.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("nik")
    val nik: String?,
    @SerializedName("nis")
    val nis: String?,
    @SerializedName("nisn")
    val nisn: String?,
    @SerializedName("school_id")
    val school: Int,
    @SerializedName("balance")
    val balance: Int,
    @SerializedName("username")
    val username: String,
): Parcelable