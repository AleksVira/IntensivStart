package ru.androidschool.intensiv.data.mapper

interface BaseMapper<FROM, TO> {
    fun mapTo(from: FROM): TO
}