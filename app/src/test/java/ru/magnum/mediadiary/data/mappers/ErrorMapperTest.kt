package ru.magnum.mediadiary.data.mappers

import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import ru.magnum.mediadiary.domain.model.AppError
import java.io.IOException

class ErrorMapperTest {

    private val mapper = ErrorMapper()

    @Test
    fun `IOException maps to Network`() {
        val error = mapper.map(IOException())

        assertEquals(AppError.Network, error)
    }

    @Test
    fun `SerializationException maps to Parsing`() {
        val error = mapper.map(SerializationException("Bad json"))

        assertEquals(AppError.Parsing, error)
    }

    @Test
    fun `404 maps to NotFound`() {
        val error = mapper.map(httpException(404))

        assertEquals(AppError.NotFound, error)
    }

    @Test
    fun `500 maps to Server`() {
        val error = mapper.map(httpException(500))

        assertEquals(AppError.Server, error)
    }

    @Test
    fun `unknown exception maps to Unknown`() {
        val error = mapper.map(IllegalStateException())

        assertEquals(AppError.Unknown, error)
    }

    private fun httpException(code: Int): HttpException {
        val body = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, body))
    }
}