package id.innovanesia.kandignas.backend.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionHistory(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("note")
    val description: String,
    @SerializedName("created_at")
    val time: String,
    @SerializedName("status")
    val status: String,
): Parcelable
