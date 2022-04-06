package ru.androidschool.intensiv.common

fun Float.convertToStars(): Float {
    return this.div(20)
}

fun Double?.voteToRating(): Float? {
    return this?.toFloat()?.times(10)
}
