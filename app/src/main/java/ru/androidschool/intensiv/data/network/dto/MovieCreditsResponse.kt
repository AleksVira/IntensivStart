package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class MovieCreditsResponse(
    val id: Int?,
    val cast: List<Cast>?,
    val crew: List<Crew>?
)