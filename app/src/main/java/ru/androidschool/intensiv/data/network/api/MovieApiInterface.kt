package ru.androidschool.intensiv.data.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.network.dto.MovieCreditsResponse
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.data.network.dto.TvShowListResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMoviesResponse(@Query("language") language: String): Call<MoviesListResponse>

    @GET("movie/upcoming")
    fun getUpcomingMoviesResponse(@Query("language") language: String): Call<MoviesListResponse>

    @GET("movie/popular")
    fun getPopularMoviesResponse(@Query("language") language: String): Call<MoviesListResponse>

    @GET("tv/popular")
    fun getTvShowsResponse(@Query("language") language: String): Call<TvShowListResponse>

    @GET("movie/{movie_id}")
    fun getMovieInfoById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Call<MovieDetailInfoResponse>

    @GET("movie/{movie_id}/credits")
    fun getMoviePersonsById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Call<MovieCreditsResponse>

}
