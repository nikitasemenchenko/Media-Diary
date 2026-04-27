package ru.magnum.mediadiary.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.magnum.mediadiary.data.local.MediaStats
import ru.magnum.mediadiary.data.remote.model.ContentType
import ru.magnum.mediadiary.data.remote.model.KinopoiskSearchDetailedResponse
import ru.magnum.mediadiary.data.remote.model.KinopoiskSearchResponse
import ru.magnum.mediadiary.data.remote.model.MediaItem
import ru.magnum.mediadiary.data.remote.model.MovieStatus
import ru.magnum.mediadiary.data.remote.model.SearchResult

interface MediaRepository {

    suspend fun search(query: String): KinopoiskSearchResponse

    suspend fun getItemById(id: Int): KinopoiskSearchDetailedResponse

    suspend fun addToWishList(item: KinopoiskSearchDetailedResponse): Boolean

    suspend fun getTrendingMovies(): List<SearchResult>

    suspend fun getTrendingSeries(): List<SearchResult>

    suspend fun getTrendingAnime(): List<SearchResult>

    suspend fun getTrendingCartoons(): List<SearchResult>

    suspend fun getTrendingAnimatedSeries(): List<SearchResult>

    fun getCollectionByStatus(status: MovieStatus): Flow<List<MediaItem>>

    suspend fun getMediaItem(id: Int): MediaItem

    suspend fun createOrUpdateItem(item: MediaItem)

    suspend fun deleteMediaItem(item: MediaItem)

    suspend fun deleteItemsByIds(ids: List<Int>)

    fun getCollectionStats(): Flow<MediaStats>

    fun getTypesCount(): Flow<Map<ContentType, Int>>

    fun getTopGenres(limit: Int = 10): Flow<List<String>>
}