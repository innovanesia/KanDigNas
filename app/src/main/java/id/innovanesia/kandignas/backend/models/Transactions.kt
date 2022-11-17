package id.innovanesia.kandignas.backend.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transactions(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("user_from")
    val host: Int,
    @SerializedName("user_to")
    val target: Int,
    @SerializedName("school_id")
    val school: Int,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("note")
    val description: String,
    @SerializedName("created_at")
    val time: String
): Parcelable