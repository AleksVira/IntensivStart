package ru.androidschool.intensiv.domain.entity

data class TvShowEntity(
    val tvShowId: Int,
    val title: String,
    val rating: Float,
    val horizontalPosterUrl: String
)