package ru.magnum.mediadiary.data.local.typeConverters

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.magnum.mediadiary.data.remote.dto.ContentType

class ContentTypeTypeConverterTest {

    val converter = ContentTypeTypeConverter()

    // Тесты для fromContentType (Enum -> String)

    @Test
    fun `fromContentType returns name`() {
        val contentType = ContentType.MOVIE
        val result = converter.fromContentType(contentType)
        assertEquals(result, "MOVIE")
    }

    @Test
    fun `fromContentType with null returns null`() {
        val result = converter.fromContentType(null)
        assertNull(result)
    }

    @Test
    fun `toContentType returns correct enum`() {
        val value = "MOVIE"
        val result = converter.toContentType(value)
        assertEquals(result, ContentType.MOVIE)
    }

    @Test
    fun `toContentType with null or blank string returns null`() {
        assertNull(converter.toContentType(null))
        assertNull(converter.toContentType(""))
        assertNull(converter.toContentType("   "))
    }

    @Test
    fun `toContentType returns null with bad value`() {
        val badValue = "random_string"
        val result = converter.toContentType(badValue)
        assertNull(result)
    }
}