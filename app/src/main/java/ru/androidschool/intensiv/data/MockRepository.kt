package ru.androidschool.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 1..10L) {
            val movie = Movie(
                movieId = x,
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTvShows(): List<Movie> =
        (1..10L).map {
            Movie(
                movieId = it,
                title = "Super Show $it",
                voteAverage = 10.0 - it
            )
        }

    fun getMovieDetails(movieId: Long): MovieDetailsEntity {
        return MovieDetailsEntity(
            movieImageUrl = "https://www.themoviedb.org/t/p/original/vBEpnpabTNAD4Boum5JIomBJYJy.jpg",
            movieName = "Красное уведомление",
            isLiked = false,
            watchLink = "",
            movieRating = 68.0f,
            movieDescription = "Действие фильма начинает своё развитие в тот момент, когда подходит к своему завершению выпущенное Интерполом «Красное уведомление». Оно предполагает высший ордер, требующий активизировать охоту и поимку самых разыскиваемых в мире людей. В центре истории оказывается опытный сотрудник ФБР по имени Джон Хартли. За свою карьеру он провёл немало задержаний. И в этот раз ему кажется, что всё пройдёт гладко. Но Хартли ошибается.",
            actorList = getActorsMockedlist(),
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
