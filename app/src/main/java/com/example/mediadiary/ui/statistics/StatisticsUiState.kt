package com.example.mediadiary.ui.statistics

import androidx.annotation.StringRes
import com.example.mediadiary.data.remote.model.ContentType

data class StatisticsUiState(
    val total: Int = 0,
    val watched: Int = 0,
    val watching: Int = 0,
    val wantToWatch: Int = 0,

    val types: Map<ContentType, Int> = emptyMap(),
    val topGenres: List<String> = emptyList(),
    @StringRes val errorMessage: Int? = null,
    val isLoading: Boolean = true
)