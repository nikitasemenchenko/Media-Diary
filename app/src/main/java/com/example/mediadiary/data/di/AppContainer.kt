package com.example.mediadiary.data.di

import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.repository.MediaRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import kotlin.jvm.java

class AppContainer {
    val mediaRepository: MediaRepository by lazy {
        MediaRepository(retrofitService)
    }
    private val BASE_URL = "https://api.kinopoisk.dev/v1.4/"
    private val json = Json {ignoreUnknownKeys = true}

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: KinopoiskApi by lazy {
        retrofit.create(KinopoiskApi::class.java)
    }
}