package ru.androidschool.intensiv.data.database.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity

@Dao
interface ActorDao {

    @Insert(onConflict = REPLACE)
    fun saveActor(actor: ActorDbEntity): Completable

    @Insert(onConflict = REPLACE)
    fun saveActors(actors: List<ActorDbEntity>): Completable

    @Delete
    fun deleteActor(actor: ActorDbEntity): Completable

}