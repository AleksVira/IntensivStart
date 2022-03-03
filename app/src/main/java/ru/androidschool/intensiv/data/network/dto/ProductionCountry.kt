package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String?,
    val name: String?
)