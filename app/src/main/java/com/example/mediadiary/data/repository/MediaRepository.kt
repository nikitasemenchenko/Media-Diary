package com.example.mediadiary.data.repository

import com.example.mediadiary.data.local.MediaDao
import com.example.mediadiary.data.local.MediaStats
import com.example.mediadiary.data.remote.KinopoiskApi
import com.example.mediadiary.data.remote.model.ContentType
import com.example.mediadiary.data.remote.model.KinopoiskSearchDetailedResponse
import com.example.mediadiary.data.remote.model.KinopoiskSearchResponse
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MediaItemMapper
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.data.remote.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MediaRepository(
    private val kpApi: KinopoiskApi,
    private val mediaDao: MediaDao,
    private val mapper: MediaItemMapper = MediaItemMapper()
) {
    private var trendingMoviesCache: List<SearchResult>? = null
    private var trendingSeriesCache: List<SearchResult>? = null
    private var trendingAnimeCache: List<SearchResult>? = null
    private var trendingCartoonsCache: List<SearchResult>? = null
    private var trendingAnimatedSeriesCache: List<SearchResult>? = null


    suspend fun search(query: String): KinopoiskSearchResponse = withContext(Dispatchers.IO) {
        val response = kpApi.multiSearch(query = query)
        val filtered = response.docs.filter { !it.poster?.url.isNullOrBlank() }
        response.copy(docs = filtered)
    }

    suspend fun getTrendingMovies(): List<SearchResult> = withContext(Dispatchers.IO) {
        if (trendingMoviesCache != null) {
            return@withContext trendingMoviesCache!!
        }
        val moviesResponse = kpApi.getTrendingMovies().docs
            .filter { !it.poster?.url.isNullOrBlank() }
        trendingMoviesCache = moviesResponse
        moviesResponse
    }

    suspend fun getTrendingSeries(): List<SearchResult> = withContext(Dispatchers.IO) {
        if (trendingSeriesCache != null) {
            return@withContext trendingSeriesCache!!
        }
        val seriesResponse = kpApi.getTrendingSeries().docs
            .filter { !it.poster?.url.isNullOrBlank() }
        trendingSeriesCache = seriesResponse
        seriesResponse
    }

    suspend fun getTrendingAnime(): List<SearchResult> = withContext(Dispatchers.IO) {
        if (trendingAnimeCache != null) {
            return@withContext trendingAnimeCache!!
        }
        val animeResponse = kpApi.getTrendingAnime().docs
            .filter { !it.poster?.url.isNullOrBlank() }
        trendingAnimeCache = animeResponse
        animeResponse
    }

    suspend fun getTrendingCartoons(): List<SearchResult> = withContext(Dispatchers.IO) {
        if (trendingCartoonsCache != null) {
            return@withContext trendingCartoonsCache!!
        }
        val cartoonResponse = kpApi.getTrendingCartoons().docs
            .filter { !it.poster?.url.isNullOrBlank() }
        trendingCartoonsCache = cartoonResponse
        cartoonResponse
    }

    suspend fun getTrendingAnimatedSeries(): List<SearchResult> = withContext(Dispatchers.IO) {
        if (trendingAnimatedSeriesCache != null) {
            return@withContext trendingAnimatedSeriesCache!!
        }
        val animatedSeriesResponse = kpApi.getTrendingAnimatedSeries().docs
            .filter { !it.poster?.url.isNullOrBlank() }
        trendingAnimatedSeriesCache = animatedSeriesResponse
        animatedSeriesResponse
    }

    suspend fun getItemById(id: Int): KinopoiskSearchDetailedResponse {
        return kpApi.getById(id = id)
    }


    suspend fun addToWishList(item: KinopoiskSearchDetailedResponse): Boolean {
        return withContext(Dispatchers.IO) {
            val newItem = mapper.toWishListItem(item)
            val response = mediaDao.insertIgnore(newItem)
            response != -1L
        }
    }

    suspend fun getMediaItem(id: Int): MediaItem {
        return withContext(Dispatchers.IO) {
            mediaDao.findById(id)?.let { return@withContext it }

            val details = kpApi.getById(id = id)
            val newItem = mapper.fromDetailedSearchResult(details)
            newItem
        }
    }

    suspend fun createOrUpdateItem(item: MediaItem) {
        return withContext(Dispatchers.IO) {
            mediaDao.insert(item)
        }
    }


    fun getCollectionByStatus(status: MovieStatus): Flow<List<MediaItem>> {
        return mediaDao.getItemsByStatus(status)
    }


    fun getTypesCount(): Flow<Map<ContentType, Int>> =
        mediaDao.getTypes().map { list ->
            list.associate { typeCount ->
                val contentType = ContentType.fromName(typeCount.type)
                    ?: ContentType.fromApiValue(typeCount.type)
                contentType to typeCount.count
            }
        }


    fun getTopGenres(limit: Int = 10): Flow<List<String>> {
        return mediaDao.getAllItems().map { items ->
            items.asSequence()
                .flatMap { it.genres?.asSequence() ?: emptySequence() }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedByDescending { it.second }
                .map { it.first }
                .take(limit)
        }
    }

    fun getCollectionStats(): Flow<MediaStats> = mediaDao.getCollectionStats()

    suspend fun deleteItemsByIds(ids: List<Int>) {
        return withContext(Dispatchers.IO) {
            mediaDao.deleteByIds(ids)
        }
    }

}