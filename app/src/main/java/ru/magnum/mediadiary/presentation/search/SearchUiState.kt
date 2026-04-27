package ru.magnum.mediadiary.presentation.search

import ru.magnum.mediadiary.domain.model.MediaPreview

data class SearchUiState(
    val isLoading: Boolean = true,

    val trendingMovies: List<MediaPreview> = emptyList(),
    val trendingSeries: List<MediaPreview> = emptyList(),
    val trendingCartoons: List<MediaPreview> = emptyList(),
    val trendingAnime: List<MediaPreview> = emptyList(),
    val trendingAnimatedSeries: List<MediaPreview> = emptyList(),

    val errorMessage: Int? = null
)