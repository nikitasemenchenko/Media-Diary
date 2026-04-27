package ru.magnum.mediadiary.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.magnum.mediadiary.domain.model.AddToCollectionResult
import ru.magnum.mediadiary.domain.model.CollectionStats
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus

interface MediaRepository {

    suspend fun search(query: String): List<MediaPreview>

    suspend fun getMediaDetails(id: Int): MediaDetails

    suspend fun addToWishList(id: Int): AddToCollectionResult

    suspend fun getTrendingMovies(): List<MediaPreview>

    suspend fun getTrendingSeries(): List<MediaPreview>

    suspend fun getTrendingAnime(): List<MediaPreview>

    suspend fun getTrendingCartoons(): List<MediaPreview>

    suspend fun getTrendingAnimatedSeries(): List<MediaPreview>

    fun getCollectionByStatus(status: WatchStatus): Flow<List<MediaDetails>>

    suspend fun saveMediaDetails(item: MediaDetails)

    suspend fun deleteMediaDetails(item: MediaDetails)

    suspend fun deleteItemsByIds(ids: List<Int>)

    fun getCollectionStats(): Flow<CollectionStats>

    fun getTypesCount(): Flow<Map<MediaType, Int>>

    fun getTopGenres(): Flow<List<String>>
}