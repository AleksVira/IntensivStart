package ru.androidschool.intensiv.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieListToShow(
    val movies: List<MovieEntity>
): Parcelable
