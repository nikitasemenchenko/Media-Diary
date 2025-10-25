package com.example.mediadiary.data.remote

import com.example.mediadiary.BuildConfig
import com.example.mediadiary.data.remote.model.KinopoiskSearchDetailedResponse
import com.example.mediadiary.data.remote.model.KinopoiskSearchResponse
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface KinopoiskApi {
    @GET("movie/search")
    suspend fun multiSearch(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 40,
        @Query("query") query: String? = null
    ): KinopoiskSearchResponse

    @GET("movie/{id}")
    suspend fun getById(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Path("id") id: Int
    ): KinopoiskSearchDetailedResponse

    @GET("movie")
    suspend fun getTrendingMovies(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 35,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "movie"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingSeries(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 35,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "tv-series"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingAnime(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 35,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "anime"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingCartoons(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 35,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "cartoon"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingAnimatedSeries(
        @Header("X-API-KEY") apiKey: String = BuildConfig.KP_API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 35,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "animated-series"
    ): KinopoiskSearchResponse
}