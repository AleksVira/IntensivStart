package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity

class MovieDetailsInfoMapper: BaseMapper<MovieDetailInfoResponse, MovieDetailsEntity> {
    override fun mapTo(from: MovieDetailInfoResponse): MovieDetailsEntity {
        return MovieDetailsEntity(
            movieImageUrl = "${BuildConfig.TMDB_RESOURCE_URL}w500${from.backdropPath}",
            movieName = from.title.orEmpty(),
            isLiked = false,
            watchLink = "",
            movieRating = from.voteAverage?.toFloat() ?: 0F,
            movieDescription = from.overview.orEmpty(),
            studioName = from.productionCompanies?.map { company ->
                company.name
            }?.joinToString().orEmpty(),
            genre = from.genres?.map { genre ->
                genre.name
            }?.joinToString()?.replaceFirstChar(Char::titlecase).orEmpty(),
            year = from.releaseDate.orEmpty()
        )
    }
}