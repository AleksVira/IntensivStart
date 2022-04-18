package ru.androidschool.intensiv.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.MovieCreditsResponse
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse

interface MovieRepository {
    fun getNowPlayingMovies(): Single<MoviesListResponse<MovieDto>>
    fun getUpcomingMovies(): Single<MoviesListResponse<MovieDto>>
    fun getPopularMovies(): Single<MoviesListResponse<MovieDto>>
    fun searchMovieByTitle(searchString: String): Observable<MoviesListResponse<MovieDto>>

    fun getMovieInfoById(movieId: Int): Single<MovieDetailInfoResponse>
    fun getPersonsByMovieId(movieId: Int): Single<MovieCreditsResponse>
}