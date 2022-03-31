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
    val page: Int? = 0,
    val results: List<T>? = emptyList(),
    @SerialName("total_pages")
    val totalPages: Int? = 0,
    @SerialName("total_results")
    val totalResults: Int? = 0
)