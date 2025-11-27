package com.example.mediadiary.ui.collection

import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus

data class CollectionUiState(
    val selectedTab: MovieStatus = MovieStatus.WANT_TO_WATCH,
    val selectedItems: Set<Int> = emptySet(),
    val items: List<MediaItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)