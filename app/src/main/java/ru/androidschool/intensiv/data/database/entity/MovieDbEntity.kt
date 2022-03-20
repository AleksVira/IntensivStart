package ru.androidschool.intensiv.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moviesManyToMany")
data class MovieDbEntity(
    @PrimaryKey val movieId: Int,
    val title: String,
    val rating: Float,
    val movieDescription: String,
    val studioName: String,
    val genre: String,
    val year: String,
    val posterUrl: String
)