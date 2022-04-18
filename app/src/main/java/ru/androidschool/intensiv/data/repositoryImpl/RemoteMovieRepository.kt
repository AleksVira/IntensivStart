package ru.androidschool.intensiv.data.repositoryImpl

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieCreditsResponse
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.domain.repository.MovieRepository
import timber.log.Timber

class RemoteMovieRepository : MovieRepository {

    override fun getNowPlayingMovies(): Single<MoviesListResponse<MovieDto>> {
        return MovieApiClient.apiClient.getNowPlayingMoviesResponse()
            .onErrorReturn {
                Timber.d("MyTAG_RemoteMovieRepository_getNowPlayingMovies(): ERROR")
                MoviesListResponse()
            }
    }

    override fun getUpcomingMovies(): Single<MoviesListResponse<MovieDto>> {
        return MovieApiClient.apiClient.getUpcomingMoviesResponse()
            .onErrorReturn {
                Timber.d("MyTAG_RemoteMovieRepository_getUpcomingMovies(): ERROR")
                MoviesListResponse()
            }
    }

    override fun getPopularMovies(): Single<MoviesListResponse<MovieDto>> {
        return MovieApiClient.apiClient.getPopularMoviesResponse()
            .onErrorReturn {
                Timber.d("MyTAG_RemoteMovieRepository_getPopularMovies(): ERROR")
                MoviesListResponse()
            }
    }

    override fun searchMovieByTitle(searchString: String): Observable<MoviesListResponse<MovieDto>> =
        MovieApiClient.apiClient.searchMovieByTitle(searchString)

    override fun getMovieInfoById(movieId: Int): Single<MovieDetailInfoResponse> =
        MovieApiClient.apiClient.getMovieInfoById(movieId)

    override fun getPersonsByMovieId(movieId: Int): Single<MovieCreditsResponse> =
        MovieApiClient.apiClient.getPersonsByMovieId(movieId)

}