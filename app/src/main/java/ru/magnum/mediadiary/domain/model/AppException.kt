package ru.magnum.mediadiary.domain.model

class AppException(
    val error: AppError,
    cause: Throwable? = null
) : RuntimeException(cause)