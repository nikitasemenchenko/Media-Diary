package ru.magnum.mediadiary.presentation.search

import AppConstants.SEARCH_DELAY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.AddToCollectionResult
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.domain.repository.MediaRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
@OptIn(FlowPreview::class)
class SearchViewModel @Inject constructor(private val repository: MediaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MediaPreview>>(emptyList())
    val searchResults: StateFlow<List<MediaPreview>> = _searchResults.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    private val _events = MutableSharedFlow<Int>()
    val events = _events.asSharedFlow()

    init {
        loadTrending()
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query == "") {
            loadTrending()
        } else {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DELAY)
                makeSearch(query)
            }
        }
    }

    private suspend fun makeSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            val response = repository.search(query = query)

            _uiState.update {
                it.copy(
                    isLoading = false,
                )
            }
            _searchResults.value = response
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = R.string.error_loading)
            }
            _searchResults.value = emptyList()
            e.printStackTrace()
        }
    }

    fun loadTrending() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                supervisorScope {
                    val movies = async { repository.getTrendingMovies() }
                    val series = async { repository.getTrendingSeries() }
                    val anime = async { repository.getTrendingAnime() }
                    val cartoons = async { repository.getTrendingCartoons() }
                    val animatedSeries = async { repository.getTrendingAnimatedSeries() }

                    val results = awaitAll(movies, series, anime, cartoons, animatedSeries)
                    _uiState.update {
                        it.copy(
                            trendingMovies = results[0],
                            trendingSeries = results[1],
                            trendingAnime = results[2],
                            trendingCartoons = results[3],
                            trendingAnimatedSeries = results[4],
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error_internet
                    )
                }
            }
        }
    }

    fun clearQuery() {
        onQueryChanged("")
    }

    fun addItemToWishlist(item: MediaPreview) {
        viewModelScope.launch {
            try {
                when (repository.addToWishList(item.id)) {
                    AddToCollectionResult.ADDED -> {
                        _events.emit(R.string.successfully_added)
                    }

                    AddToCollectionResult.ALREADY_EXISTS -> {
                        _events.emit(R.string.already_in_collection)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _events.emit(R.string.error_adding)
            }
        }
    }

}