package id.innovanesia.kandignas.backend.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val image: Int,
) : Parcelable
