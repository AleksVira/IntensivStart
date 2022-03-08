package ru.androidschool.intensiv.domain.entity

data class MovieEntity(
    val movieId: Int,
    val title: String,
    val voteAverage: Double,
    val posterUrl: String
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
