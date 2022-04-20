package ru.androidschool.intensiv.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse

interface MovieSearchRepository {
    fun searchMovies(searchString: String, lang: String): Observable<MoviesListResponse<MovieDto>>
}