package ru.androidschool.intensiv.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieEntity(
    val movieId: Int,
    val title: String,
    val rating: Float,
    val posterUrl: String,
    val horizontalPosterUrl: String
) : Parcelable
