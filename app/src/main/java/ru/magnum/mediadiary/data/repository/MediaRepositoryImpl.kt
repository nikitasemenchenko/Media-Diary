package ru.magnum.mediadiary.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.magnum.mediadiary.data.local.MediaDao
import ru.magnum.mediadiary.data.mappers.ErrorMapper
import ru.magnum.mediadiary.data.mappers.MediaMapper
import ru.magnum.mediadiary.data.remote.KinopoiskApi
import ru.magnum.mediadiary.data.remote.dto.ContentType
import ru.magnum.mediadiary.domain.model.AddToCollectionResult
import ru.magnum.mediadiary.domain.model.AppException
import ru.magnum.mediadiary.domain.model.CollectionStats
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.domain.repository.MediaRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class MediaRepositoryImpl @Inject constructor(
    private val kpApi: KinopoiskApi,
    private val mediaDao: MediaDao,
    private val mapper: MediaMapper,
    private val errorMapper: ErrorMapper
): MediaRepository {
    private var trendingMoviesCache: List<MediaPreview>? = null
    private var trendingSeriesCache: List<MediaPreview>? = null
    private var trendingAnimeCache: List<MediaPreview>? = null
    private var trendingCartoonsCache: List<MediaPreview>? = null
    private var trendingAnimatedSeriesCache: List<MediaPreview>? = null


    override suspend fun search(query: String): List<MediaPreview> = runCatchingAppError {
         kpApi.multiSearch(query = query)
            .docs
            .map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.title.isNullOrBlank() && !it.poster.isNullOrBlank() }
    }

    override suspend fun getTrendingMovies(page: Int): List<MediaPreview> = runCatchingAppError  {
        trendingMoviesCache?.let {
            return@runCatchingAppError it }

        kpApi.getTrendingMovies(page = page)
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .shuffled()
            .also { trendingMoviesCache = it }
    }

    override suspend fun getTrendingSeries(page: Int): List<MediaPreview> = runCatchingAppError  {
        trendingSeriesCache?.let {
            return@runCatchingAppError it }

        kpApi.getTrendingSeries(page = page)
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .shuffled()
            .also { trendingSeriesCache = it }
    }

    override suspend fun getTrendingAnime(page: Int): List<MediaPreview>  = runCatchingAppError {
        trendingAnimeCache?.let {
            return@runCatchingAppError it }

        kpApi.getTrendingAnime(page = page)
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .shuffled()
            .also { trendingAnimeCache = it }
    }

    override suspend fun getTrendingCartoons(page: Int): List<MediaPreview> = runCatchingAppError {
        trendingCartoonsCache?.let {
            return@runCatchingAppError it }

        kpApi.getTrendingCartoons(page = page)
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .shuffled()
            .also { trendingCartoonsCache = it }
    }

    override suspend fun getTrendingAnimatedSeries(page: Int): List<MediaPreview> = runCatchingAppError {
        trendingAnimatedSeriesCache?.let {
            return@runCatchingAppError it }

        kpApi.getTrendingAnimatedSeries(page = page)
            .docs.map { doc ->
                mapper.searchResultToPreview(doc)
            }
            .filter { !it.poster.isNullOrBlank() }
            .shuffled()
            .also { trendingAnimatedSeriesCache = it }
    }

    override suspend fun deleteMediaDetails(item: MediaDetails) = runCatchingAppError {
        mediaDao.delete(mapper.detailsToEntity(item))
    }


    override suspend fun addToWishList(id: Int): AddToCollectionResult  = runCatchingAppError {
        val fullItem = kpApi.getById(id)
        val entity = mapper.detailedResponseToWishlistEntity(fullItem)
        val result = mediaDao.insertIgnore(entity)

        if (result == -1L) {
            AddToCollectionResult.ALREADY_EXISTS
        } else {
            AddToCollectionResult.ADDED
        }
    }

    override suspend fun getMediaDetails(id: Int): MediaDetails = runCatchingAppError {
        mediaDao.findById(id)?.let { item ->
            return@runCatchingAppError mapper.entityToDetails(item)
        }

        val details = kpApi.getById(id = id)
        mapper.detailedResponseToDetails(details)
    }

    override suspend fun saveMediaDetails(item: MediaDetails) = runCatchingAppError {
            mediaDao.insert(mapper.detailsToEntity(item))
    }


    override fun getCollectionByStatus(status: WatchStatus): Flow<List<MediaDetails>> {
        val dataStatus = mapper.watchStatusToData(status)

        requireNotNull(dataStatus)

        return mediaDao.getItemsByStatus(dataStatus).map { items ->
            items.map { item ->
                mapper.entityToDetails(item)
            }
        }.catch { e ->
            if (e is CancellationException) throw e
            throw e.toAppException()
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

        }.catch { e ->
            if (e is CancellationException) throw e
            throw e.toAppException()
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
        }.catch { e ->
            if (e is CancellationException) throw e
            throw e.toAppException()
        }
    }

    override fun getCollectionStats(): Flow<CollectionStats> {
        return mediaDao.getCollectionStats().map { stats ->
            mapper.statsToDomain(stats)
        }.catch { e ->
            if (e is CancellationException) throw e
            throw e.toAppException()
        }
    }

    override suspend fun deleteItemsByIds(ids: List<Int>) = runCatchingAppError  {
        mediaDao.deleteByIds(ids)
    }

    private fun Throwable.toAppException(): AppException {
        return if (this is AppException) {
            this
        } else {
            AppException(
                error = errorMapper.map(this),
                cause = this
            )
        }
    }

    private suspend fun <T> runCatchingAppError(
        block: suspend () -> T
    ): T {
        return try {
            block()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e.toAppException()
        }
    }

}