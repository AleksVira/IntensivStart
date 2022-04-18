package ru.androidschool.intensiv.presentation.movie_details.viewmodel

import ru.androidschool.intensiv.core.CoreViewEvent

sealed class MovieDetailsViewEvent : CoreViewEvent {
    object IconClicked: MovieDetailsViewEvent()
}