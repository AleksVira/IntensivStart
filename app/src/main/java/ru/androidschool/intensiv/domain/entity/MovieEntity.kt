package ru.androidschool.intensiv.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieEntity(
    val movieId: Int,
    val title: String,
    val voteAverage: Double,
    val posterUrl: String,
    val horizontalPosterUrl: String
) : Parcelable {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
