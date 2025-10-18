package com.example.mediadiary.data.repository

import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.remote.model.KinopoiskSearchResponse
import com.example.mediadiary.data.remote.model.SearchResult

class MediaRepository(
private val kpApi: KinopoiskApi
){
    private var trendingCache: List<SearchResult>? = null

    suspend fun search(query: String): KinopoiskSearchResponse {
        return kpApi.multiSearch(query = query)
    }
    suspend fun getTrendings(): List<SearchResult> {
        if (trendingCache != null) {
            return trendingCache!!
        }
        val moviesResponse = kpApi.getTrendingMovies()
        val seriesResponse = kpApi.getTrendingSeries()
        val animeResponse = kpApi.getTrendingAnime()

        val combined = (moviesResponse.docs + seriesResponse.docs+ animeResponse.docs).shuffled()
        trendingCache = combined

        return combined
    }

}