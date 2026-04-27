package ru.magnum.mediadiary.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.magnum.mediadiary.data.local.MediaDao
import ru.magnum.mediadiary.data.mappers.MediaMapper
import ru.magnum.mediadiary.data.remote.KinopoiskApi
import ru.magnum.mediadiary.data.remote.dto.ContentType
import ru.magnum.mediadiary.domain.model.AddToCollectionResult
import ru.magnum.mediadiary.domain.model.CollectionStats
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.domain.repository.MediaRepository
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val kpApi: KinopoiskApi,
    private val mediaDao: MediaDao,
    private val mapper: MediaMapper
): MediaRepository {
    private var trendingMoviesCache: List<MediaPreview>? = null
    private var trendingSeriesCache: List<MediaPreview>? = null
    private var trendingAnimeCache: List<MediaPreview>? = null
    private var trendingCartoonsCache: List<MediaPreview>? = null
    private var trendingAnimatedSeriesCache: List<MediaPreview>? = null


    override suspend fun search(query: String): List<MediaPreview> {
        return kpApi.multiSearch(query = query)
            .docs
            .map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
    }

    override suspend fun getTrendingMovies(): List<MediaPreview> {
        trendingMoviesCache?.let { return it }

        return kpApi.getTrendingMovies()
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .also { trendingMoviesCache = it }
    }

    override suspend fun getTrendingSeries(): List<MediaPreview> {
        trendingSeriesCache?.let { return it }

        return kpApi.getTrendingSeries()
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .also { trendingSeriesCache = it }
    }

    override suspend fun getTrendingAnime(): List<MediaPreview> {
        trendingAnimeCache?.let { return it }

        return kpApi.getTrendingAnime()
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .also { trendingAnimeCache = it }
    }

    override suspend fun getTrendingCartoons(): List<MediaPreview> {
        trendingCartoonsCache?.let { return it }

        return kpApi.getTrendingCartoons()
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .also { trendingCartoonsCache = it }
    }

    override suspend fun getTrendingAnimatedSeries(): List<MediaPreview> {
        trendingAnimatedSeriesCache?.let { return it }

        return kpApi.getTrendingAnimatedSeries()
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .also { trendingAnimatedSeriesCache = it }
    }

    override suspend fun deleteMediaDetails(item: MediaDetails){
        mediaDao.delete(mapper.detailsToEntity(item))
    }


    override suspend fun addToWishList(id: Int): AddToCollectionResult {
        val fullItem = kpApi.getById(id)
        val entity = mapper.detailedResponseToWishlistEntity(fullItem)
        val result = mediaDao.insertIgnore(entity)

        return if (result == -1L) {
            AddToCollectionResult.ALREADY_EXISTS
        } else {
            AddToCollectionResult.ADDED
        }
    }

    override suspend fun getMediaDetails(id: Int): MediaDetails  {
        mediaDao.findById(id)?.let { return mapper.entityToDetails(it) }

        val details = kpApi.getById(id = id)
        return mapper.detailedResponseToDetails(details)
    }

    override suspend fun saveMediaDetails(item: MediaDetails) {
            mediaDao.insert(mapper.detailsToEntity(item))
    }


    override fun getCollectionByStatus(status: WatchStatus): Flow<List<MediaDetails>> {
        val dataStatus = mapper.watchStatusToData(status)

        requireNotNull(dataStatus)

        return mediaDao.getItemsByStatus(dataStatus).map { items ->
            items.map { item ->
                mapper.entityToDetails(item)
            }
        }
    }


    override fun getTypesCount(): Flow<Map<MediaType, Int>> {
        return mediaDao.getTypes().map { list ->
            list.mapNotNull { typeCount ->
                val contentType = ContentType.fromName(typeCount.type)
                val mediaType = mapper.contentTypeToDomain(contentType)

                mediaType?.let {
                    it to typeCount.count
                }
            }.toMap()
        }
    }


    override fun getTopGenres(): Flow<List<String>> {
        return mediaDao.getAllItems().map { items ->
            items
                .flatMap { it.genres.orEmpty() }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedByDescending { it.second }
                .take(10)
                .map { it.first }
        }
    }

    override fun getCollectionStats(): Flow<CollectionStats> {
        return mediaDao.getCollectionStats().map { stats ->
            mapper.statsToDomain(stats)
        }
    }

    override suspend fun deleteItemsByIds(ids: List<Int>) {
        mediaDao.deleteByIds(ids)
    }

}