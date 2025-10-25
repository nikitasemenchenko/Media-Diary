package com.example.mediadiary.data.repository

import com.example.mediadiary.data.local.MediaDao
import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.remote.model.KinopoiskSearchDetailedResponse
import com.example.mediadiary.data.remote.model.KinopoiskSearchResponse
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MediaRepository(
private val kpApi: KinopoiskApi,
    private val mediaDao: MediaDao
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
        val cartoonsResponse = kpApi.getTrendingCartoons()
        val animatesSeriesResponse = kpApi.getTrendingAnimatedSeries()

        val combined = (moviesResponse.docs + seriesResponse.docs+ animeResponse.docs
                + cartoonsResponse.docs + animatesSeriesResponse.docs).shuffled()
        trendingCache = combined

        return combined
    }
    suspend fun getItemById(id: Int): KinopoiskSearchDetailedResponse{
        return kpApi.getById(id = id)
    }


    suspend fun addToWishList(item: KinopoiskSearchDetailedResponse): Boolean {
        return withContext(Dispatchers.IO) {
            val check = mediaDao.findById(item.id)
            if (check == null) {
                val newItem = MediaItem.fromDetailedSearchResult(item)
                mediaDao.insert(newItem)
                true
            } else {
                false
            }
        }
    }

    fun getCollection(): Flow<List<MediaItem>> = mediaDao.getAllItems()


}