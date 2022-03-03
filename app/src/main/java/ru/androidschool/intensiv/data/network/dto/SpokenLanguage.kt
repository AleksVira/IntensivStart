package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class SpokenLanguage(
    @SerialName("english_name")
    val englishName: String?,
    @SerialName("iso_639_1")
    val iso6391: String?,
    val name: String?
)