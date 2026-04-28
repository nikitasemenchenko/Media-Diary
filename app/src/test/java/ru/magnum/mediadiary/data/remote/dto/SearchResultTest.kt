package ru.magnum.mediadiary.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SearchResultTest {

    @Test
    fun `getItemTitle returns name first`() {
        val result = SearchResult(
            id = 1,
            name = "Русское название",
            alternativeName = "Alternative",
            enName = "English",
            names = listOf(KinopoiskName(name = "names"))
        )

        assertEquals("Русское название", result.getItemTitle())
    }

    @Test
    fun `getItemTitle returns alternativeName`() {
        val result = SearchResult(
            id = 1,
            name = null,
            alternativeName = "Alternative",
            enName = "English",
            names = listOf(KinopoiskName(name = "names"))
        )

        assertEquals("Alternative", result.getItemTitle())
    }

    @Test
    fun `getItemTitle returns enName`() {
        val result = SearchResult(
            id = 1,
            name = null,
            alternativeName = null,
            enName = "English",
            names = listOf(KinopoiskName(name = "From names"))
        )

        assertEquals("English", result.getItemTitle())
    }

    @Test
    fun `getItemTitle returns name from names list`() {
        val result = SearchResult(
            id = 1,
            name = null,
            alternativeName = null,
            enName = null,
            names = listOf(
                KinopoiskName(name = ""),
                KinopoiskName(name = "From names")
            )
        )

        assertEquals("From names", result.getItemTitle())
    }

    @Test
    fun `getItemTitle returns null when all names are blank`() {
        val result = SearchResult(
            id = 1,
            name = "",
            alternativeName = "",
            enName = "",
            names = listOf(KinopoiskName(name = ""))
        )

        assertNull(result.getItemTitle())
    }

    @Test
    fun `getPosterUrl returns poster url`() {
        val result = SearchResult(
            id = 1,
            poster = KinopoiskPoster(
                url = "full_url",
                previewUrl = "preview_url"
            )
        )

        assertEquals("full_url", result.getPosterUrl())
    }

    @Test
    fun `getPosterUrl returns preview url`() {
        val result = SearchResult(
            id = 1,
            poster = KinopoiskPoster(
                url = "",
                previewUrl = "preview_url"
            )
        )

        assertEquals("preview_url", result.getPosterUrl())
    }

    @Test
    fun `getPosterUrl returns null when poster is empty`() {
        val result = SearchResult(
            id = 1,
            poster = KinopoiskPoster(
                url = "",
                previewUrl = ""
            )
        )

        assertNull(result.getPosterUrl())
    }

    @Test
    fun `getItemRating returns max rating`() {
        val result = SearchResult(
            id = 1,
            rating = KinopoiskRating(
                kp = 7.2,
                imdb = 8.1
            )
        )

        assertEquals(8.1, result.getItemRating()!!, 0.0)
    }

    @Test
    fun `getItemRating ignores zero ratings`() {
        val result = SearchResult(
            id = 1,
            rating = KinopoiskRating(
                kp = 0.0,
                imdb = 0.0
            )
        )

        assertNull(result.getItemRating())
    }
}