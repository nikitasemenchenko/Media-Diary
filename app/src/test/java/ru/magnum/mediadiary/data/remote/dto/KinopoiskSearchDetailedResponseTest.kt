package ru.magnum.mediadiary.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class KinopoiskSearchDetailedResponseTest {

    @Test
    fun `getItemTitle return name from names list`() {
        val result = KinopoiskSearchDetailedResponse(
            id = 1,
            name = null,
            alternativeName = null,
            enName = null,
            type = "movie",
            names = listOf(
                KinopoiskName(name = ""),
                KinopoiskName(name = "Название из names")
            )
        )

        assertEquals("Название из names", result.getItemTitle())
    }

    @Test
    fun `getPosterUrl returns preview url`() {
        val result = KinopoiskSearchDetailedResponse(
            id = 1,
            type = "movie",
            poster = KinopoiskPoster(
                url = "",
                previewUrl = "preview_url"
            )
        )

        assertEquals("preview_url", result.getPosterUrl())
    }

    @Test
    fun `getAge returns age with plus`() {
        val result = KinopoiskSearchDetailedResponse(
            id = 1,
            type = "movie",
            ageRating = 18
        )

        assertEquals("18+", result.getAge())
    }

    @Test
    fun `getDuration returns series length`() {
        val result = KinopoiskSearchDetailedResponse(
            id = 1,
            type = "tv-series",
            movieLength = null,
            seriesLength = 45
        )

        assertEquals("45", result.getDuration())
    }
}