package ru.androidschool.intensiv.common

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.prepare(): Observable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.prepare(): Single<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.prepare(): Completable {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}