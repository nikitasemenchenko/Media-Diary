package com.example.mediadiary.data.remote

import com.example.mediadiary.data.remote.model.KinopoiskSearchDetailedResponse
import com.example.mediadiary.data.remote.model.KinopoiskSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @GET("movie/search")
    suspend fun multiSearch(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 40,
        @Query("query") query: String? = null
    ): KinopoiskSearchResponse

    @GET("movie/{id}")
    suspend fun getById(
        @Path("id") id: Int
    ): KinopoiskSearchDetailedResponse

    @GET("movie")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "movie"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingSeries(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "tv-series"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "anime"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingCartoons(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "cartoon"
    ): KinopoiskSearchResponse

    @GET("movie")
    suspend fun getTrendingAnimatedSeries(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("sortField") sortField: String = "votes.imdb",
        @Query("sortType") sortType: String = "-1",
        @Query("type") type: String = "animated-series"
    ): KinopoiskSearchResponse
}