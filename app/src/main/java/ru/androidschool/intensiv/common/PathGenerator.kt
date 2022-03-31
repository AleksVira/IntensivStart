package ru.androidschool.intensiv.common

import ru.androidschool.intensiv.BuildConfig

private const val CONFIG_WIDTH: String = "w500"

fun generateImagePath(path: String) =
    "${BuildConfig.TMDB_RESOURCE_URL}$CONFIG_WIDTH$path"
