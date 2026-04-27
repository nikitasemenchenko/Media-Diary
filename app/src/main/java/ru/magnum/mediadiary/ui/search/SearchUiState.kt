package ru.magnum.mediadiary.ui.search

import ru.magnum.mediadiary.data.remote.model.SearchResult

data class SearchUiState(
    val isLoading: Boolean = true,

    val trendingMovies: List<SearchResult> = emptyList(),
    val trendingSeries: List<SearchResult> = emptyList(),
    val trendingCartoons: List<SearchResult> = emptyList(),
    val trendingAnime: List<SearchResult> = emptyList(),
    val trendingAnimatedSeries: List<SearchResult> = emptyList(),

    val errorMessage: Int? = null
)