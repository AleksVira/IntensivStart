package ru.androidschool.intensiv.data.network.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.androidschool.intensiv.BuildConfig

object MovieApiClient {

    internal const val API_KEY_HEADER = "api_key"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(MovieApiHeaderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply{
            this.level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @ExperimentalSerializationApi
    val apiClient: MovieApiInterface by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(httpClient)
            .build()

        return@lazy retrofit.create(MovieApiInterface::class.java)
    }

}
