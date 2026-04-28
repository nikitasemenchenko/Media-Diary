package ru.magnum.mediadiary.data.local.typeConverters

import org.junit.Assert.assertEquals
import org.junit.Test

class GenreListTypeConverterTest {

    private val converter = GenreListTypeConverter()

    @Test
    fun `list converts to string and back`() {
        val genres = listOf("драма", "боевик", "аниме")

        val toString = converter.fromList(genres)
        val fromString = converter.toList(toString)

        assertEquals(genres, fromString)
    }

    @Test
    fun `null list converts to empty list`() {
        val encoded = converter.fromList(null)
        val decoded = converter.toList(encoded)

        assertEquals(emptyList<String>(), decoded)
    }

    @Test
    fun `blank string converts to empty list`() {
        val decoded = converter.toList("")

        assertEquals(emptyList<String>(), decoded)
    }

    @Test
    fun `bad json converts to empty list`() {
        val decoded = converter.toList("bad json")

        assertEquals(emptyList<String>(), decoded)
    }
}