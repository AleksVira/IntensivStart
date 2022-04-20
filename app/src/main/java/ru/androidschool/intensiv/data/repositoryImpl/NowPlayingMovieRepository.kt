package ru.androidschool.intensiv.data.repositoryImpl

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.domain.repository.MovieRepository
import timber.log.Timber

class NowPlayingMovieRepository : MovieRepository {

    override fun getMovies(lang: String): Single<MoviesListResponse<MovieDto>> {
        return MovieApiClient.apiClient.getNowPlayingMoviesResponse(lang)
            .onErrorReturn {
                Timber.d("MyTAG_NowPlayingMovieRepository_getMovies(): ERROR")
                MoviesListResponse()
            }
    }

}