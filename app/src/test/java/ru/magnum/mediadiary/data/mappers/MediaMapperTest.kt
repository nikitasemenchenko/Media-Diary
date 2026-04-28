package ru.magnum.mediadiary.data.mappers

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.magnum.mediadiary.data.local.MediaItem
import ru.magnum.mediadiary.data.local.MovieStatus
import ru.magnum.mediadiary.data.remote.dto.ContentType
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus

class MediaMapperTest {

    private val mapper = MediaMapper()

    @Test
    fun `movie status maps to watch status`() {
        assertEquals(
            WatchStatus.WANT_TO_WATCH,
            mapper.movieStatusToDomain(MovieStatus.WANT_TO_WATCH)
        )

        assertEquals(
            WatchStatus.WATCHING,
            mapper.movieStatusToDomain(MovieStatus.WATCHING)
        )

        assertEquals(
            WatchStatus.WATCHED,
            mapper.movieStatusToDomain(MovieStatus.WATCHED)
        )
    }

    @Test
    fun `watch status maps to movie status`() {
        assertEquals(
            MovieStatus.WANT_TO_WATCH,
            mapper.watchStatusToData(WatchStatus.WANT_TO_WATCH)
        )

        assertEquals(
            MovieStatus.WATCHING,
            mapper.watchStatusToData(WatchStatus.WATCHING)
        )

        assertEquals(
            MovieStatus.WATCHED,
            mapper.watchStatusToData(WatchStatus.WATCHED)
        )
    }

    @Test
    fun `content type maps to media type`() {
        assertEquals(MediaType.MOVIE, mapper.contentTypeToDomain(ContentType.MOVIE))
        assertEquals(MediaType.TV_SERIES, mapper.contentTypeToDomain(ContentType.TV_SERIES))
        assertEquals(MediaType.CARTOON, mapper.contentTypeToDomain(ContentType.CARTOON))
        assertEquals(MediaType.ANIME, mapper.contentTypeToDomain(ContentType.ANIME))
        assertEquals(MediaType.ANIMATED_SERIES,mapper.contentTypeToDomain(ContentType.ANIMATED_SERIES)
        )
    }

    @Test
    fun `entity maps to details`() {
        val entity = MediaItem(
            id = 1,
            title = "Test title",
            year = 2024,
            description = "Description",
            type = ContentType.MOVIE,
            rating = 8.5,
            poster = "poster_url",
            genres = listOf("драма", "боевик"),
            ageRating = "16+",
            director = "Director",
            actors = "Actor 1, Actor 2",
            countries = "США",
            length = "120",
            watchStatus = MovieStatus.WATCHED,
            userRating = 9,
            watchDate = 1000L,
            addedAt = 500L,
            userNote = "Good"
        )

        val details = mapper.entityToDetails(entity)

        assertEquals(1, details.id)
        assertEquals("Test title", details.title)
        assertEquals(2024, details.year)
        assertEquals(MediaType.MOVIE, details.type)
        assertEquals(WatchStatus.WATCHED, details.watchStatus)
        assertEquals(9, details.userRating)
        assertEquals(1000L, details.watchDate)
        assertEquals(500L, details.addedAt)
        assertEquals("Good", details.userNote)
        assertEquals(listOf("драма", "боевик"), details.genres)
    }

    @Test
    fun `details maps to entity`() {
        val details = MediaDetails(
            id = 2,
            title = "Details title",
            year = 2020,
            description = "Description",
            type = MediaType.TV_SERIES,
            rating = 7.8,
            poster = "poster_url",
            genres = listOf("драма"),
            ageRating = "18+",
            director = "Director",
            actors = "Actor",
            countries = "Корея",
            length = "60",
            watchStatus = WatchStatus.WATCHED,
            userRating = null,
            watchDate = null,
            addedAt = 100L,
            userNote = "Note"
        )

        val entity = mapper.detailsToEntity(details)

        assertEquals(2, entity.id)
        assertEquals("Details title", entity.title)
        assertEquals(ContentType.TV_SERIES, entity.type)
        assertEquals(MovieStatus.WATCHED, entity.watchStatus)
        assertNull(entity.userRating)
        assertNull(entity.watchDate)
        assertEquals(100L, entity.addedAt)
        assertEquals("Note", entity.userNote)
    }
}