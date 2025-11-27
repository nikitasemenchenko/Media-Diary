package com.example.mediadiary.ui.details

import androidx.annotation.StringRes
import com.example.mediadiary.data.remote.model.MediaItem

sealed class MediaDetailUiState {
    object Loading : MediaDetailUiState()

    data class Success(
        val item: MediaItem
    ) : MediaDetailUiState()

    data class Error(
        @StringRes val message: Int
    ) : MediaDetailUiState()
}