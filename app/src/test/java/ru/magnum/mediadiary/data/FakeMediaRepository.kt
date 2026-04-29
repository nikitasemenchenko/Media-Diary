package ru.magnum.mediadiary.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.magnum.mediadiary.domain.model.AddToCollectionResult
import ru.magnum.mediadiary.domain.model.CollectionStats
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.domain.repository.MediaRepository

class FakeMediaRepository : MediaRepository {

    var mediaDetails: MediaDetails? = null
    var exception: Throwable? = null

    var savedItem: MediaDetails? = null
    var deletedItem: MediaDetails? = null
    var deletedIds: List<Int>? = null

    override suspend fun search(query: String): List<MediaPreview> = emptyList()

    override suspend fun getMediaDetails(id: Int): MediaDetails {
        exception?.let { throw it }
        return mediaDetails ?: error("no mediaDetails")
    }

    override suspend fun addToWishList(id: Int): AddToCollectionResult {
        return AddToCollectionResult.ADDED
    }

    override suspend fun getTrendingMovies(): List<MediaPreview> = emptyList()

    override suspend fun getTrendingSeries(): List<MediaPreview> = emptyList()

    override suspend fun getTrendingAnime(): List<MediaPreview> = emptyList()

    override suspend fun getTrendingCartoons(): List<MediaPreview> = emptyList()

    override suspend fun getTrendingAnimatedSeries(): List<MediaPreview> = emptyList()

    override fun getCollectionByStatus(status: WatchStatus): Flow<List<MediaDetails>> {
        return MutableStateFlow(emptyList())
    }

    override suspend fun saveMediaDetails(item: MediaDetails) {
        exception?.let { throw it }
        savedItem = item
    }

    override suspend fun deleteMediaDetails(item: MediaDetails) {
        exception?.let { throw it }
        deletedItem = item
    }

    override suspend fun deleteItemsByIds(ids: List<Int>) {
        deletedIds = ids
    }

    override fun getCollectionStats(): Flow<CollectionStats> {
        return MutableStateFlow(
            CollectionStats(
                total = 0,
                watched = 0,
                watching = 0,
                wantToWatch = 0
            )
        )
    }

    override fun getTypesCount(): Flow<Map<MediaType, Int>> {
        return MutableStateFlow(emptyMap())
    }

    override fun getTopGenres(): Flow<List<String>> {
        return MutableStateFlow(emptyList())
    }
}