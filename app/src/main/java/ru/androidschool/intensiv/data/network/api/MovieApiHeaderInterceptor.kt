package ru.androidschool.intensiv.data.network.api

import okhttp3.Interceptor
import okhttp3.Response
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.network.api.MovieApiClient.API_KEY_HEADER


class MovieApiHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(API_KEY_HEADER, BuildConfig.THE_MOVIE_DATABASE_API)
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(newRequest)
    }
}