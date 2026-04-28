package ru.magnum.mediadiary.data.remote.dto

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ContentTypeTest {

    @Test
    fun `fromApiValue returns correct content type`() {
        assertEquals(ContentType.MOVIE, ContentType.fromApiValue("movie"))
        assertEquals(ContentType.TV_SERIES, ContentType.fromApiValue("tv-series"))
        assertEquals(ContentType.CARTOON, ContentType.fromApiValue("cartoon"))
        assertEquals(ContentType.ANIME, ContentType.fromApiValue("anime"))
        assertEquals(ContentType.ANIMATED_SERIES, ContentType.fromApiValue("animated-series"))
    }

    @Test
    fun `fromApiValue returns null for unknown value`() {
        assertNull(ContentType.fromApiValue(null))
        assertNull(ContentType.fromApiValue(""))
    }

    @Test
    fun `fromName returns correct content type`() {
        assertEquals(ContentType.MOVIE, ContentType.fromName("MOVIE"))
        assertEquals(ContentType.TV_SERIES, ContentType.fromName("TV_SERIES"))
    }
}