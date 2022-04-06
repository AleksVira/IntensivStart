package ru.androidschool.intensiv.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actorsManyToMany")
data class ActorDbEntity(
    @PrimaryKey val actorId: Int,
    val imageUrl: String,
    val fullName: String
)