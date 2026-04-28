package ru.magnum.mediadiary.data.local.typeConverters

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.magnum.mediadiary.data.local.MovieStatus

class MovieStatusTypeConverterTest {

    private val converter = MovieStatusTypeConverter()

    @Test
    fun `fromMovieStatus returns enum name`() {
        assertEquals(
            "WATCHED",
            converter.fromMovieStatus(MovieStatus.WATCHED)
        )
    }

    @Test
    fun `fromMovieStatus returns null for null`() {
        assertNull(converter.fromMovieStatus(null))
    }

    @Test
    fun `toMovieStatus returns enum`() {
        assertEquals(
            MovieStatus.WATCHING,
            converter.toMovieStatus("WATCHING")
        )
    }

    @Test
    fun `toMovieStatus returns null for bad value`() {
        assertNull(converter.toMovieStatus("BAD_VALUE"))
    }

    @Test
    fun `toMovieStatus returns null for blank value`() {
        assertNull(converter.toMovieStatus(""))
    }
}