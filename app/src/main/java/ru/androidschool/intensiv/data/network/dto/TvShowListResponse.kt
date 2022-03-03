package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import ru.androidschool.intensiv.data.network.dto.TvShowDto

@Keep
@Serializable
data class TvShowListResponse(
    val page: Int?,
    val results: List<TvShowDto>?,
    @SerialName("total_pages")
    val totalPages: Int?,
    @SerialName("total_results")
    val totalResults: Int?
)