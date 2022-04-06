package ru.androidschool.intensiv.common

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.prepare(): Observable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.prepare(
    background: Scheduler = Schedulers.io(),
    foreground: Scheduler = AndroidSchedulers.mainThread()
): Single<T> {
    return this
        .subscribeOn(background)
        .observeOn(foreground)
}

fun Completable.prepare(): Completable {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}