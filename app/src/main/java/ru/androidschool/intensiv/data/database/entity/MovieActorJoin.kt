package ru.androidschool.intensiv.data.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieActorJoin(
    val movieId: Int,
    val actorId: Int
)