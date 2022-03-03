package ru.androidschool.intensiv.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class TvShowDto(
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("first_air_date")
    val firstAirDate: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>?,
    val id: Int?,
    val name: String?,
    @SerialName("origin_country")
    val originCountry: List<String>?,
    @SerialName("original_language")
    val originalLanguage: String?,
    @SerialName("original_name")
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("vote_count")
    val voteCount: Int?
)