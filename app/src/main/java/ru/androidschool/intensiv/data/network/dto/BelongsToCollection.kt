package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class BelongsToCollection(
    val id: Int?,
    val name: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?
)