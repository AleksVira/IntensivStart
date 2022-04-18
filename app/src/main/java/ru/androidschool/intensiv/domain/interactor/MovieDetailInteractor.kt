package ru.androidschool.intensiv.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity
import ru.androidschool.intensiv.data.network.dto.MovieCreditsResponse
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.data.repositoryImpl.LocalMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.RemoteMovieRepository

class MovieDetailInteractor(
    private val remoteMovieRepo: RemoteMovieRepository,
    private val localMovieRepo: LocalMovieRepository
) {

    fun getMovieInfoById(movieId: Int): Single<MovieDetailInfoResponse> {
        return remoteMovieRepo.getMovieInfoById(movieId)
    }

    fun getPersonsByMovieId(movieId: Int): Single<MovieCreditsResponse> {
        return remoteMovieRepo.getPersonsByMovieId(movieId)
    }

    fun checkSavedMovieById(movieId: Int): Single<Boolean> {
        return localMovieRepo.checkSavedMovieById(movieId)
    }

    fun saveToDb(
        currentDetailsForDb: MovieDbEntity,
        currentActors: List<ActorDbEntity>
    ): Completable {
        return localMovieRepo.saveToDb(currentDetailsForDb, currentActors)
    }

    fun deleteMovieById(movieId: Int): Completable {
        return localMovieRepo.deleteMovieById(movieId)
    }

}