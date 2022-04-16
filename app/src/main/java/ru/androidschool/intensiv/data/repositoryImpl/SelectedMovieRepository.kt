package ru.androidschool.intensiv.data.repositoryImpl

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.api.*
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieActorJoin
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity

class SelectedMovieRepository(
    private val context: Context,
    private val movieDao: MovieDao = SelectedMovieDatabase(context).movieDao(),
    private val actorDao: ActorDao = SelectedMovieDatabase(context).actorDao(),
    private val movieWithActorsDao: MovieWithActorsDao = SelectedMovieDatabase(context).movieWithActorDao()
) {
    fun saveToDb(
        currentDetailsForDb: MovieDbEntity,
        currentActors: List<ActorDbEntity>
    ): Completable {
        val resultOne = movieDao.saveMovie(currentDetailsForDb)
        val resultTwo = actorDao.saveActors(currentActors)
        val resultThree = currentActors.map { actorEntity ->
            MovieActorJoin(
                movieId = currentDetailsForDb.movieId,
                actorId = actorEntity.actorId
            )
        }.let {
            movieWithActorsDao.saveJoins(it)
        }
        return resultOne.andThen(resultTwo).andThen(resultThree)
    }

    fun getSelectedMovies(): Observable<List<MovieWithActors>> {
        return movieWithActorsDao.getAllSelectedMovies()
    }

    fun checkSavedMovieById(movieId: Int): Single<Boolean> {
        return movieDao.isExist(movieId)
    }

    fun deleteMovieById(movieId: Int): Completable {
        return movieDao.deleteById(movieId)
    }


}