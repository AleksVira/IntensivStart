package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.domain.entity.MovieEntity

class MovieDtoMapper: BaseMapper<MovieDto, MovieEntity> {
    override fun mapTo(from: MovieDto): MovieEntity {
        return MovieEntity(
            movieId = from.id ?: 0,
            title = from.title.orEmpty(),
            voteAverage = from.voteAverage ?: 0.0,
            posterUrl = "${BuildConfig.TMDB_RESOURCE_URL}w500${from.posterPath}",
            horizontalPosterUrl = "${BuildConfig.TMDB_RESOURCE_URL}w500${from.backdropPath}"

        )
    }
}