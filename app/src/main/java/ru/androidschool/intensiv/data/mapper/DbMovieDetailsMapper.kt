package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.common.generateImagePath
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity

class DbMovieDetailsMapper : BaseMapper<MovieDetailsEntity, MovieDbEntity> {

    override fun mapTo(from: MovieDetailsEntity): MovieDbEntity {
        return MovieDbEntity(
            movieId = from.id,
            title = from.movieName,
            rating = from.movieRating,
            movieDescription = from.movieDescription,
            studioName = from.studioName,
            genre = from.genre,
            year = from.year,
            posterUrl = generateImagePath(from.posterPath)
        )
    }
}