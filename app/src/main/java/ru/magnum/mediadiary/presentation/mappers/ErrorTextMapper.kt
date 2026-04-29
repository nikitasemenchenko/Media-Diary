package ru.magnum.mediadiary.presentation.mappers

import androidx.annotation.StringRes
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.AppError

@StringRes
fun AppError.messageRes(): Int {
    return when (this) {
        AppError.Network -> R.string.error_internet
        AppError.Server -> R.string.error_server
        AppError.NotFound -> R.string.error_not_found
        AppError.Unauthorized -> R.string.error_unauthorized
        AppError.Parsing -> R.string.error_parsing
        AppError.Unknown -> R.string.error_unknown
    }
}