package ru.androidschool.intensiv.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.api.MovieWithActors
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity

interface DbMovieRepository {

    fun saveToDb(
        currentDetailsForDb: MovieDbEntity,
        currentActors: List<ActorDbEntity>
    ): Completable

    fun getSelectedMovies(): Observable<List<MovieWithActors>>

    fun checkSavedMovieById(movieId: Int): Single<Boolean>

    fun deleteMovieById(movieId: Int): Completable

}