package id.innovanesia.kandignas.backend.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schools(
    @SerializedName("name")
    val name: String
): Parcelable
