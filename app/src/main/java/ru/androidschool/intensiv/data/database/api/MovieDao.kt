package ru.androidschool.intensiv.data.database.api

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity

@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE)
    fun saveMovie(movie: MovieDbEntity): Completable

    @Delete
    fun deleteMovie(movie: MovieDbEntity): Completable

    @Query("SELECT EXISTS (SELECT 1 FROM moviesManyToMany WHERE movieId = :movieId)")
    fun isExist(movieId: Int): Single<Boolean>

    @Transaction
    @Query("DELETE FROM moviesManyToMany WHERE movieId = :movieId")
    fun deleteById(movieId: Int): Completable

}