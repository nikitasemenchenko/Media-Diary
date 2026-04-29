package ru.magnum.mediadiary.domain.model

sealed interface AppError {
    data object Network : AppError
    data object Server : AppError
    data object NotFound : AppError
    data object Unauthorized: AppError
    data object Parsing : AppError
    data object Unknown : AppError
}