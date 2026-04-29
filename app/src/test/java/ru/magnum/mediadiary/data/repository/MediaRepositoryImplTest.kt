package ru.magnum.mediadiary.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.magnum.mediadiary.data.local.FakeMediaDao
import ru.magnum.mediadiary.data.local.MediaItem
import ru.magnum.mediadiary.data.local.MovieStatus
import ru.magnum.mediadiary.data.mappers.ErrorMapper
import ru.magnum.mediadiary.data.mappers.MediaMapper
import ru.magnum.mediadiary.data.remote.FakeKinopoiskApi
import ru.magnum.mediadiary.data.remote.dto.ContentType
import ru.magnum.mediadiary.data.remote.dto.KinopoiskGenres
import ru.magnum.mediadiary.data.remote.dto.KinopoiskPoster
import ru.magnum.mediadiary.data.remote.dto.KinopoiskRating
import ru.magnum.mediadiary.data.remote.dto.KinopoiskSearchDetailedResponse
import ru.magnum.mediadiary.data.remote.dto.KinopoiskSearchResponse
import ru.magnum.mediadiary.data.remote.dto.SearchResult
import ru.magnum.mediadiary.domain.model.AddToCollectionResult
import ru.magnum.mediadiary.domain.model.AppException
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import java.io.IOException

class MediaRepositoryImplTest {

    private lateinit var api: FakeKinopoiskApi
    private lateinit var dao: FakeMediaDao
    private lateinit var repository: MediaRepositoryImpl

    @Before
    fun setUp() {
        api = FakeKinopoiskApi()
        dao = FakeMediaDao()

        repository = MediaRepositoryImpl(
            kpApi = api,
            mediaDao = dao,
            mapper = MediaMapper(),
            errorMapper = ErrorMapper()
        )
    }

    @Test
    fun `getMediaDetails returns local item when it exists in database`() = runTest {
        val localItem = testMediaItem(
            id = 1,
            title = "Local title",
            watchStatus = MovieStatus.WATCHED
        )

        dao.setItems(listOf(localItem))

        api.detailResponse = testDetailedResponse(
            id = 1,
            title = "Remote title"
        )

        val result = repository.getMediaDetails(1)

        assertEquals("Local title", result.title)
        assertEquals(WatchStatus.WATCHED, result.watchStatus)
    }

    @Test
    fun `getMediaDetails loads from api when local item doesnt exist`() = runTest {
        api.detailResponse = testDetailedResponse(
            id = 2,
            title = "Remote title"
        )

        val result = repository.getMediaDetails(2)

        assertEquals("Remote title", result.title)
        assertEquals(null, result.watchStatus)
    }

    @Test
    fun `addToWishList inserts item and returns ADDED`() = runTest {
        api.detailResponse = testDetailedResponse(
            id = 3,
            title = "Added title"
        )

        val result = repository.addToWishList(3)

        val storedItem = dao.getStoredItem(3)

        assertEquals(AddToCollectionResult.ADDED, result)
        assertNotNull(storedItem)
        assertEquals("Added title", storedItem?.title)
        assertEquals(MovieStatus.WANT_TO_WATCH, storedItem?.watchStatus)
    }

    @Test
    fun `addToWishList returns ALREADY_EXISTS when dao ignores insert`() = runTest {
        val existingItem = testMediaItem(
            id = 4,
            title = "Existing",
            watchStatus = MovieStatus.WANT_TO_WATCH
        )

        dao.setItems(listOf(existingItem))

        api.detailResponse = testDetailedResponse(
            id = 4,
            title = "Existing"
        )

        val result = repository.addToWishList(4)

        assertEquals(AddToCollectionResult.ALREADY_EXISTS, result)
    }

    @Test
    fun `saveMediaDetails stores updated user fields`() = runTest {
        val details = repository.getMediaDetailsFromTestItem(
            id = 5,
            title = "Saved title",
            status = WatchStatus.WATCHED
        )

        repository.saveMediaDetails(
            details.copy(
                userRating = 9,
                watchDate = 123456L,
                userNote = "Good"
            )
        )

        val storedItem = dao.getStoredItem(5)

        assertNotNull(storedItem)
        assertEquals(MovieStatus.WATCHED, storedItem?.watchStatus)
        assertEquals(9, storedItem?.userRating)
        assertEquals(123456L, storedItem?.watchDate)
        assertEquals("Good", storedItem?.userNote)
    }

    @Test
    fun `deleteItemsByIds removes selected items`() = runTest {
        dao.setItems(
            listOf(
                testMediaItem(id = 1, title = "One"),
                testMediaItem(id = 2, title = "Two"),
                testMediaItem(id = 3, title = "Three")
            )
        )

        repository.deleteItemsByIds(listOf(1, 3))

        val items = dao.getAllItems().first()

        assertEquals(listOf(2), items.map { it.id })
    }

    @Test
    fun `getCollectionByStatus returns only selected status`() = runTest {
        dao.setItems(
            listOf(
                testMediaItem(id = 1, title = "One", watchStatus = MovieStatus.WANT_TO_WATCH),
                testMediaItem(id = 2, title = "Two", watchStatus = MovieStatus.WATCHING),
                testMediaItem(id = 3, title = "Three", watchStatus = MovieStatus.WATCHED)
            )
        )

        val result = repository.getCollectionByStatus(WatchStatus.WATCHING).first()

        assertEquals(1, result.size)
        assertEquals(2, result.first().id)
        assertEquals(WatchStatus.WATCHING, result.first().watchStatus)
    }

    @Test
    fun `getCollectionStats returns correct counts`() = runTest {
        dao.setItems(
            listOf(
                testMediaItem(id = 1, title = "One", watchStatus = MovieStatus.WANT_TO_WATCH),
                testMediaItem(id = 2, title = "Two", watchStatus = MovieStatus.WATCHING),
                testMediaItem(id = 3, title = "Three", watchStatus = MovieStatus.WATCHED),
                testMediaItem(id = 4, title = "Four", watchStatus = MovieStatus.WATCHED)
            )
        )

        val stats = repository.getCollectionStats().first()

        assertEquals(4, stats.total)
        assertEquals(2, stats.watched)
        assertEquals(1, stats.watching)
        assertEquals(1, stats.wantToWatch)
    }

    @Test
    fun `getTrendingMovies uses cache after first call`() = runTest {
        api.trendingMoviesResponse = KinopoiskSearchResponse(
            docs = listOf(
                testSearchResult(id = 10, title = "Trending")
            )
        )

        val first = repository.getTrendingMovies()
        val second = repository.getTrendingMovies()

        assertEquals(1, first.size)
        assertEquals(1, second.size)
        assertEquals(1, api.trendingMoviesCallCount)
    }

    @Test
    fun `api IOException is mapped to AppException`() = runTest {
        api.exception = IOException("No internet")

        val result = runCatching {
            repository.getMediaDetails(100)
        }

        assertTrue(result.exceptionOrNull() is AppException)
    }

    private fun testMediaItem(
        id: Int,
        title: String,
        watchStatus: MovieStatus? = MovieStatus.WANT_TO_WATCH
    ): MediaItem {
        return MediaItem(
            id = id,
            title = title,
            year = 2024,
            description = "Description",
            type = ContentType.MOVIE,
            rating = 8.0,
            poster = "poster",
            genres = listOf("драма"),
            ageRating = "16+",
            director = "Director",
            actors = "Actor",
            countries = "США",
            length = "120",
            watchStatus = watchStatus,
            userRating = null,
            watchDate = null,
            addedAt = 0L,
            userNote = null
        )
    }

    private fun testDetailedResponse(
        id: Int,
        title: String
    ): KinopoiskSearchDetailedResponse {
        return KinopoiskSearchDetailedResponse(
            id = id,
            name = title,
            alternativeName = null,
            enName = null,
            year = 2024,
            description = "Description",
            type = "movie",
            rating = KinopoiskRating(kp = 8.0, imdb = 7.0),
            poster = KinopoiskPoster(url = "poster", previewUrl = null),
            genres = listOf(KinopoiskGenres(name = "драма")),
            countries = emptyList(),
            movieLength = 120,
            seriesLength = null,
            ageRating = 16,
            persons = emptyList()
        )
    }

    private fun testSearchResult(
        id: Int,
        title: String
    ): SearchResult {
        return SearchResult(
            id = id,
            name = title,
            alternativeName = null,
            enName = null,
            year = 2024,
            type = "movie",
            poster = KinopoiskPoster(url = "poster", previewUrl = null),
            genres = listOf(KinopoiskGenres(name = "драма")),
            rating = KinopoiskRating(kp = 8.0, imdb = 7.0)
        )
    }

    private suspend fun MediaRepositoryImpl.getMediaDetailsFromTestItem(
        id: Int,
        title: String,
        status: WatchStatus
    ) = ru.magnum.mediadiary.domain.model.MediaDetails(
        id = id,
        title = title,
        year = 2024,
        description = "Description",
        type = MediaType.MOVIE,
        rating = 8.0,
        poster = "poster",
        genres = listOf("драма"),
        ageRating = "16+",
        director = "Director",
        actors = "Actor",
        countries = "США",
        length = "120",
        watchStatus = status,
        userRating = null,
        watchDate = null,
        addedAt = 0L,
        userNote = null
    )
}