package ru.androidschool.intensiv.domain.entity

data class MovieDetailsEntity(
    val id: Int,
    val movieImageUrl: String,
    val movieName: String,
    val watchLink: String,
    val movieRating: Float,
    val movieDescription: String,
    val studioName: String,
    val genre: String,
    val year: String,
    val posterPath: String
)
