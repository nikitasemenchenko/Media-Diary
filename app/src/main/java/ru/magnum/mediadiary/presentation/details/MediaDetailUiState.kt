package ru.magnum.mediadiary.presentation.details

import androidx.annotation.StringRes
import ru.magnum.mediadiary.domain.model.MediaDetails

sealed class MediaDetailUiState {
    object Loading : MediaDetailUiState()

    data class Success(
        val item: MediaDetails
    ) : MediaDetailUiState()

    data class Error(
        @StringRes val message: Int
    ) : MediaDetailUiState()
}