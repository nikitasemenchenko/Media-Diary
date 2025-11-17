package com.example.mediadiary.data.repository

import com.example.mediadiary.data.local.MediaDao
import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.remote.model.KinopoiskSearchDetailedResponse
import com.example.mediadiary.data.remote.model.KinopoiskSearchResponse
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MediaStats
import com.example.mediadiary.data.remote.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MediaRepository(
    private val kpApi: KinopoiskApi,
    private val mediaDao: MediaDao
) {
    private var trendingMoviesCache: List<SearchResult>? = null
    private var trendingSeriesCache: List<SearchResult>? = null
    private var trendingAnimeCache: List<SearchResult>? = null
    private var trendingCartoonsCache: List<SearchResult>? = null
    private var trendingAnimatedSeriesCache: List<SearchResult>? = null


    suspend fun search(query: String): KinopoiskSearchResponse {
        val response = kpApi.multiSearch(query = query)
        val filtered = response.docs.filter { !it.poster?.url.isNullOrBlank() }
        return response.copy(docs = filtered)
    }

    suspend fun updateMediaItem(newItem: MediaItem) {
        mediaDao.update(newItem)
    }

    suspend fun getTrendingMovies(): List<SearchResult> {
        if (trendingMoviesCache != null) {
            return trendingMoviesCache!!
        }
        val moviesResponse = kpApi.getTrendingMovies().docs
            .filter { !it.poster?.url.isNullOrBlank() }
            .shuffled()
        trendingMoviesCache = moviesResponse
        return moviesResponse
    }

    suspend fun getTrendingSeries(): List<SearchResult> {
        if (trendingSeriesCache != null) {
            return trendingSeriesCache!!
        }
        val seriesResponse = kpApi.getTrendingSeries().docs
            .filter { !it.poster?.url.isNullOrBlank() }
            .shuffled()
        trendingSeriesCache = seriesResponse
        return seriesResponse
    }

    suspend fun getTrendingAnime(): List<SearchResult> {
        if (trendingAnimeCache != null) {
            return trendingAnimeCache!!
        }
        val animeResponse = kpApi.getTrendingAnime().docs
            .filter { !it.poster?.url.isNullOrBlank() }
            .shuffled()
        trendingAnimeCache = animeResponse
        return animeResponse
    }

    suspend fun getTrendingCartoons(): List<SearchResult> {
        if (trendingCartoonsCache != null) {
            return trendingCartoonsCache!!
        }
        val cartoonResponse = kpApi.getTrendingCartoons().docs
            .filter { !it.poster?.url.isNullOrBlank() }
            .shuffled()
        trendingCartoonsCache = cartoonResponse
        return cartoonResponse
    }

    suspend fun getTrendingAnimatedSeries(): List<SearchResult> {
        if (trendingAnimatedSeriesCache != null) {
            return trendingAnimatedSeriesCache!!
        }
        val animatedSeriesResponse = kpApi.getTrendingAnimatedSeries().docs
            .filter { !it.poster?.url.isNullOrBlank() }
            .shuffled()
        trendingAnimatedSeriesCache = animatedSeriesResponse
        return animatedSeriesResponse
    }

    suspend fun getItemById(id: Int): KinopoiskSearchDetailedResponse {
        return kpApi.getById(id = id)
    }


    suspend fun addToWishList(item: KinopoiskSearchDetailedResponse): Boolean {
        return withContext(Dispatchers.IO) {
            val check = mediaDao.findById(item.id)
            if (check == null) {
                val newItem = MediaItem.onAddToWishList(item)
                mediaDao.insert(newItem)
                true
            } else {
                false
            }
        }
    }

    suspend fun getOrCreateMediaItem(id: Int): MediaItem {
        return withContext(Dispatchers.IO) {
            val existing = mediaDao.findById(id)
            if (existing != null) {
                return@withContext existing
            }

            val details = kpApi.getById(id = id)
            val newItem = MediaItem.fromDetailedSearchResult(details)
            mediaDao.insert(newItem)
            newItem
        }
    }

    suspend fun createOrUpdateItem(item: MediaItem) {
        return withContext(Dispatchers.IO) {
            val check = mediaDao.findById(item.id)
            if (check == null) {
                mediaDao.insert(item)
            } else {
                mediaDao.update(item)
            }
        }
    }

    suspend fun deleteMediaItem(item: MediaItem){
        withContext(Dispatchers.IO){
            mediaDao.delete(item)
        }
    }


    fun getCollection(): Flow<List<MediaItem>> = mediaDao.getAllItems()

    suspend fun getMediaStats(): MediaStats {
        return withContext(Dispatchers.IO) {
            val total = mediaDao.getTotalCount()
            val watched = mediaDao.getWatchedCount()
            val watching = mediaDao.getWatchingCount()
            val wantToWatch = mediaDao.getWantToWatchCount()
            MediaStats(total, watched, watching, wantToWatch)

        }
    }

}