package com.example.mediadiary.data.di

import android.content.Context
import com.example.mediadiary.data.AppConstants.BASE_URL
import com.example.mediadiary.data.local.MediaDiaryDatabase
import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.repository.MediaRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainer(private val context: Context) {
    val mediaRepository: MediaRepository by lazy {
        MediaRepository(
            retrofitService,
            MediaDiaryDatabase.getDatabase(context).mediaDao()
        )

    }
    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: KinopoiskApi by lazy {
        retrofit.create(KinopoiskApi::class.java)
    }

}