package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse

interface MovieRepository {
    fun getMovies(lang: String): Single<MoviesListResponse<MovieDto>>
}