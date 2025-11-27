package com.example.mediadiary.data.di

import AppConstants
import android.content.Context
import com.example.mediadiary.BuildConfig
import com.example.mediadiary.data.local.MediaDiaryDatabase
import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.remote.model.MediaItemMapper
import com.example.mediadiary.data.repository.MediaRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AppContainer(private val context: Context) {

    private val mediaItemMapper: MediaItemMapper by lazy { MediaItemMapper() }

    val mediaRepository: MediaRepository by lazy {
        MediaRepository(
            retrofitService,
            MediaDiaryDatabase.getDatabase(context).mediaDao(),
            mediaItemMapper
        )
    }

    val okHttp = OkHttpClient.Builder()
        .connectTimeout(AppConstants.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .readTimeout(AppConstants.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .writeTimeout(AppConstants.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .addInterceptor { chain ->
            val req = chain.request().newBuilder()
                .addHeader("X-API-KEY", BuildConfig.KP_API_KEY)
                .build()
            chain.proceed(req)
        }
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: KinopoiskApi by lazy {
        retrofit.create(KinopoiskApi::class.java)
    }

}