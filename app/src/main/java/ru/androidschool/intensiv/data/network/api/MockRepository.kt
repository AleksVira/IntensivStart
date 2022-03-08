package ru.androidschool.intensiv.data.network.api

import ru.androidschool.intensiv.domain.entity.ActorInfoEntity
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import ru.androidschool.intensiv.domain.entity.MovieEntity

object MockRepository {

    fun getMovies(): List<MovieEntity> {

        val moviesList = mutableListOf<MovieEntity>()
        for (x in 1..10) {
            val movie = MovieEntity(
                movieId = x,
                title = "Spider-Man $x",
                voteAverage = 10.0 - x,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg"
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTvShows(): List<MovieEntity> =
        (1..10).map {
            MovieEntity(
                movieId = it,
                title = "Super Show $it",
                voteAverage = 10.0 - it,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg"
            )
        }

    fun getMovieDetails(movieId: Int): MovieDetailsEntity {
        return MovieDetailsEntity(
            movieImageUrl = "https://www.themoviedb.org/t/p/original/vBEpnpabTNAD4Boum5JIomBJYJy.jpg",
            movieName = "Красное уведомление",
            isLiked = false,
            watchLink = "",
            movieRating = 68.0f,
            movieDescription = "Действие фильма начинает своё развитие в тот момент, когда подходит к своему завершению выпущенное Интерполом «Красное уведомление». Оно предполагает высший ордер, требующий активизировать охоту и поимку самых разыскиваемых в мире людей. В центре истории оказывается опытный сотрудник ФБР по имени Джон Хартли. За свою карьеру он провёл немало задержаний. И в этот раз ему кажется, что всё пройдёт гладко. Но Хартли ошибается.",
            studioName = "Red Notice Studio",
            genre = "боевик, комедия, криминал, триллер",
            year ="2021"
        )
    }

    private fun getActorsMockedlist(): List<ActorInfoEntity> =
        (1..6).map {
            ActorInfoEntity(
                imageUrl = "https://www.themoviedb.org/t/p/original/viKVMpGgsKRrzl5ZqkzrR3RJQxn.jpg",
                fullName = "Dwayne Johnson $it"
            )
        }

}
