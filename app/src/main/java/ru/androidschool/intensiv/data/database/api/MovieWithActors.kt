package ru.androidschool.intensiv.data.database.api

import androidx.room.*
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieActorJoin
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity

data class MovieWithActors(
    @Embedded
    val movie: MovieDbEntity,
    @Relation(
        parentColumn = "movieId",
        entity = ActorDbEntity::class,
        entityColumn = "actorId",
        associateBy = Junction(
            value = MovieActorJoin::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<ActorDbEntity>
)
