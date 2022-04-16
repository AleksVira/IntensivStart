package ru.androidschool.intensiv.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.data.repositoryImpl.RemoteMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.SelectedMovieRepository
import ru.androidschool.intensiv.presentation.feed.UnitedMoviesList

class FeedInteractor(
    private val remoteMovieRepo: RemoteMovieRepository
) {
    fun getFeedMovie(): Single<UnitedMoviesList> {
        val getNowPlayingMovies = remoteMovieRepo.getNowPlayingMovies()
        val getUpcomingMovies = remoteMovieRepo.getUpcomingMovies()
        val getPopularMovies = remoteMovieRepo.getPopularMovies()

        return Single.zip(
            getNowPlayingMovies,
            getUpcomingMovies,
            getPopularMovies
        ) { nowPlayingList: MoviesListResponse<MovieDto>,
            upcomingList: MoviesListResponse<MovieDto>,
            popularList: MoviesListResponse<MovieDto> ->
            UnitedMoviesList(nowPlayingList, upcomingList, popularList)
        }

    }

    fun searchMovie(searchString: String): Observable<MoviesListResponse<MovieDto>> {
        return remoteMovieRepo.searchMovieByTitle(searchString)
    }

}