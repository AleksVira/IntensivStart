package ru.androidschool.intensiv.presentation.movie_details.viewmodel

import ru.androidschool.intensiv.core.CoreViewState
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import ru.androidschool.intensiv.presentation.movie_details.ActorInfoItem

sealed class MovieDetailsViewState : CoreViewState {
    data class StateMovieData(val movieDetailsEntity: MovieDetailsEntity) : MovieDetailsViewState()
    data class StatePersonsData(val actorsList: List<ActorInfoItem>) : MovieDetailsViewState()
    data class StateIcon(val isSaved: Boolean) : MovieDetailsViewState()
}