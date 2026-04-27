package ru.magnum.mediadiary.presentation.mappers

import androidx.annotation.StringRes
import ru.magnum.mediadiary.domain.model.AppError
import ru.magnum.mediadiary.domain.model.AppException

@StringRes
fun Throwable.toMessageRes(): Int {
    val error = (this as? AppException)?.error ?: AppError.Unknown
    return error.messageRes()
}