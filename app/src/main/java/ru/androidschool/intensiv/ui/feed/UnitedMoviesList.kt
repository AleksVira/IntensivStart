package ru.androidschool.intensiv.ui.feed

import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse

data class UnitedMoviesList(
    val nowPlayingList: MoviesListResponse<MovieDto>,
    val upcomingList: MoviesListResponse<MovieDto>,
    val popularList: MoviesListResponse<MovieDto>
)
