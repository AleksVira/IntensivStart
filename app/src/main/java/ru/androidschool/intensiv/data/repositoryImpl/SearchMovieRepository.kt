package ru.androidschool.intensiv.data.repositoryImpl

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.domain.repository.MovieRepository
import ru.androidschool.intensiv.domain.repository.MovieSearchRepository
import timber.log.Timber

class SearchMovieRepository : MovieSearchRepository {

    override fun searchMovies(
        searchString: String,
        lang: String
    ): Observable<MoviesListResponse<MovieDto>> {
        return MovieApiClient.apiClient.searchMovieByTitle(searchString, lang)
    }

}