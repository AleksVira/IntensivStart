package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse

interface MovieRepository {
    fun getNowPlayingMovies(): Single<MoviesListResponse<MovieDto>>
    fun getUpcomingMovies(): Single<MoviesListResponse<MovieDto>>
    fun getPopularMovies(): Single<MoviesListResponse<MovieDto>>
}