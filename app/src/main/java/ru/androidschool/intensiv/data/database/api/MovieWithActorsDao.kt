package ru.androidschool.intensiv.data.database.api

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Observable
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieActorJoin
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity

@Dao
interface MovieWithActorsDao {
    @Insert(onConflict = REPLACE)
    fun saveJoins(joins: List<MovieActorJoin>): Completable

    @Transaction
    @Query("SELECT * FROM moviesManyToMany")
    fun getAllSelectedMovies(): Observable<List<MovieWithActors>>

    @Transaction
    @Query("SELECT * FROM moviesManyToMany WHERE movieId = :movieId")
    fun loadByMovieId(movieId: Int): Observable<MovieWithActors>

    @Query("DELETE FROM moviesManyToMany")
    fun deleteAll(): Completable

}