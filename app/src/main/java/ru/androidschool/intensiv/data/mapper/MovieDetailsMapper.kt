package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.common.generateImagePath
import ru.androidschool.intensiv.common.voteToRating
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity

class MovieDetailsMapper : BaseMapper<MovieDetailInfoResponse, MovieDetailsEntity> {
    override fun mapTo(from: MovieDetailInfoResponse): MovieDetailsEntity {
        return MovieDetailsEntity(
            id = from.id ?: 0,
            movieImageUrl = generateImagePath(from.backdropPath.orEmpty()),
            movieName = from.title.orEmpty(),
            watchLink = "",
            movieRating = from.voteAverage?.voteToRating() ?: 0F,
            movieDescription = from.overview.orEmpty(),
            studioName = from.productionCompanies?.map { company ->
                company.name
            }?.joinToString().orEmpty(),
            genre = from.genres?.map { genre ->
                genre.name
            }?.joinToString()?.replaceFirstChar(Char::titlecase).orEmpty(),
            year = from.releaseDate.orEmpty(),
            posterPath = from.posterPath.orEmpty()
        )
    }
}