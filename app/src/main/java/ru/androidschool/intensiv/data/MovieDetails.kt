package ru.androidschool.intensiv.data

data class MovieDetailsEntity(
    val movieImageUrl: String,
    val movieName: String,
    var isLiked: Boolean,
    val watchLink: String,
    val movieRating: Float,
    val movieDescription: String,
    val actorList: List<ActorInfoEntity>,
    val studioName: String,
    val genre: String,
    val year: String,
)
