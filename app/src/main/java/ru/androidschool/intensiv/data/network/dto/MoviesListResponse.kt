package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import ru.androidschool.intensiv.data.network.dto.Dates
import ru.androidschool.intensiv.data.network.dto.MovieDto

@Keep
@Serializable
data class MoviesListResponse<T>(
    val dates: Dates? = null,
    val page: Int?,
    val results: List<T>?,
    @SerialName("total_pages")
    val totalPages: Int?,
    @SerialName("total_results")
    val totalResults: Int?
)