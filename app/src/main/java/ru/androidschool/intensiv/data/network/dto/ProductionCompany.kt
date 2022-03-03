package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class ProductionCompany(
    val id: Int?,
    @SerialName("logo_path")
    val logoPath: String? = null,
    val name: String?,
    @SerialName("origin_country")
    val originCountry: String?
)