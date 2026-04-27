package ru.magnum.mediadiary.presentation.statistics

import androidx.annotation.StringRes
import ru.magnum.mediadiary.domain.model.MediaType

data class StatisticsUiState(
    val total: Int = 0,
    val watched: Int = 0,
    val watching: Int = 0,
    val wantToWatch: Int = 0,

    val types: Map<MediaType, Int> = emptyMap(),
    val topGenres: List<String> = emptyList(),
    @StringRes val errorMessage: Int? = null,
    val isLoading: Boolean = true
)