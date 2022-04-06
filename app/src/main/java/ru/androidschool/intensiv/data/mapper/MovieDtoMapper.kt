package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.common.generateImagePath
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.domain.entity.MovieEntity

class MovieDtoMapper : BaseMapper<MovieDto, MovieEntity> {
    override fun mapTo(from: MovieDto): MovieEntity {
        return MovieEntity(
            movieId = from.id ?: 0,
            title = from.title.orEmpty(),
            rating = from.voteAverage?.div(2)?.toFloat() ?: 0F,
            posterUrl = generateImagePath(from.posterPath.orEmpty()),
            horizontalPosterUrl = generateImagePath(from.backdropPath.orEmpty())
        )
    }
}