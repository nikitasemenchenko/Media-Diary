package com.example.mediadiary.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.AppConstants.SEARCH_DELAY
import com.example.mediadiary.data.AppConstants.adding_error
import com.example.mediadiary.data.AppConstants.already_in_collection
import com.example.mediadiary.data.AppConstants.successfully_added
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.SearchResult
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MediaRepository) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _trendingMovies = MutableStateFlow<List<SearchResult>>(emptyList())
    val trendingMovies: StateFlow<List<SearchResult>> = _trendingMovies.asStateFlow()

    private val _trendingSeries = MutableStateFlow<List<SearchResult>>(emptyList())
    val trendingSeries: StateFlow<List<SearchResult>> = _trendingSeries.asStateFlow()

    private val _trendingAnime = MutableStateFlow<List<SearchResult>>(emptyList())
    val trendingAnime: StateFlow<List<SearchResult>> = _trendingAnime.asStateFlow()

    private val _trendingCartoon = MutableStateFlow<List<SearchResult>>(emptyList())
    val trendingCartoon: StateFlow<List<SearchResult>> = _trendingCartoon.asStateFlow()

    private val _trendingAnimatedSeries = MutableStateFlow<List<SearchResult>>(emptyList())
    val trendingAnimatedSeries: StateFlow<List<SearchResult>> =
        _trendingAnimatedSeries.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()


    fun loadTrendings() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _trendingMovies.value = repository.getTrendingMovies()
                _trendingSeries.value = repository.getTrendingSeries()
                _trendingAnime.value = repository.getTrendingAnime()
                _trendingCartoon.value = repository.getTrendingCartoons()
                _trendingAnimatedSeries.value = repository.getTrendingAnimatedSeries()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        loadTrendings()
    }


    fun makeSearch(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.search(query = query)
                _searchResults.value = response.docs
            } catch (e: Exception) {
                _searchResults.value = emptyList()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query

        searchJob?.cancel()

        if (query == "") {
            loadTrendings()
        } else {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DELAY)
                makeSearch(query)
            }
        }
    }

    fun addItemToWishlist(item: SearchResult) {
        viewModelScope.launch {
            try {
                val fullItem = repository.getItemById(item.id)
                val added = repository.addToWishList(fullItem)
                if (added) {
                    _events.emit(successfully_added)
                } else {
                    _events.emit(already_in_collection)
                }
            } catch (e: Exception) {
                _events.emit(adding_error)
                Log.d("addingToWishlist", e.message ?: "произошла ошибка")
            }
        }
    }

    fun getMediaItemById(id: Int, onSuccess: (MediaItem) -> Unit) {
        viewModelScope.launch {
            try {
                val item = repository.getOrCreateMediaItem(id)
                onSuccess(item)
            } catch (e: Exception) {
                Log.d("getMediaItemById", "${e.message}")
            }
        }
    }
}