package ru.magnum.mediadiary.data.mappers

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import ru.magnum.mediadiary.domain.model.AppError
import java.io.IOException
import javax.inject.Inject

class ErrorMapper @Inject constructor() {
    fun map(e: Throwable): AppError {
        return when(e) {
            is IOException -> AppError.Network
            is SerializationException -> AppError.Parsing
            is HttpException -> {
                when(e.code()) {
                    404 -> AppError.NotFound
                    401 -> AppError.Unauthorized
                    else -> AppError.Server
                }
            }
            else -> AppError.Unknown
        }
    }
}