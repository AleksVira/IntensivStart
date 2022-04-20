package ru.androidschool.intensiv.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.common.MoviesType
import ru.androidschool.intensiv.common.MoviesType.*
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.data.repositoryImpl.NowPlayingMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.PopularMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.SearchMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.UpcomingMovieRepository

class FeedInteractor(
    private val nowPlayingMovieRepository: NowPlayingMovieRepository,
    private val popularMovieRepository: PopularMovieRepository,
    private val upcomingMovieRepository: UpcomingMovieRepository,
    private val searchMovieRepo: SearchMovieRepository,
) {
    fun getFeedMovie(lang: String): Single<Map<MoviesType, MoviesListResponse<MovieDto>>> {
        val getNowPlayingMovies = nowPlayingMovieRepository.getMovies(lang)
        val getUpcomingMovies = upcomingMovieRepository.getMovies(lang)
        val getPopularMovies = popularMovieRepository.getMovies(lang)

        return Single.zip(
            getNowPlayingMovies,
            getUpcomingMovies,
            getPopularMovies
        ) { nowPlayingList: MoviesListResponse<MovieDto>,
            upcomingList: MoviesListResponse<MovieDto>,
            popularList: MoviesListResponse<MovieDto> ->
            mapOf(
                NOW_PLAYING to nowPlayingList,
                UPCOMING to upcomingList,
                POPULAR to popularList
            )
        }
    }

    fun searchMovieByTitle(
        searchString: String,
        lang: String
    ): Observable<MoviesListResponse<MovieDto>> {
        return searchMovieRepo.searchMovies(searchString, lang)
    }

}