package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class Genre(
    val id: Int?,
    val name: String?
)