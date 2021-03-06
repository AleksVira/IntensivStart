package ru.androidschool.intensiv.data.network.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.network.dto.*

interface MovieApiInterface {
    companion object {
        const val DEFAULT_LANGUAGE = "en"
    }

    @GET("movie/now_playing")
    fun getNowPlayingMoviesResponse(
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Single<MoviesListResponse<MovieDto>>

    @GET("movie/upcoming")
    fun getUpcomingMoviesResponse(
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Single<MoviesListResponse<MovieDto>>

    @GET("movie/popular")
    fun getPopularMoviesResponse(
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Single<MoviesListResponse<MovieDto>>

    @GET("tv/popular")
    fun getTvShowsResponse(
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Single<MoviesListResponse<TvShowDto>>

    @GET("search/movie")
    fun searchMovieByTitle(
        @Query("query") query: String,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Observable<MoviesListResponse<MovieDto>>

    @GET("movie/{movie_id}")
    fun getMovieInfoById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Single<MovieDetailInfoResponse>

    @GET("movie/{movie_id}/credits")
    fun getMoviePersonsById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Single<MovieCreditsResponse>
}
