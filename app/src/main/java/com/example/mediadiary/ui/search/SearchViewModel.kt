package com.example.mediadiary.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SearchViewModel(private val repository: MediaRepository): ViewModel() {
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()
    val already_in_collection = "Уже в коллекции"
    val successfully_added = "Добавлено в \"Хочу посмотреть\""
    val error = "Ошибка"


    fun loadTrendings(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getTrendings()
                _searchResults.value = response
            }
            catch (e: Exception) {
                _searchResults.value = emptyList()
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    init {
        loadTrendings()
    }


    fun makeSearch(query: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.search(query = query)
                _searchResults.value = response.docs
            }
            catch (e: Exception) {
                _searchResults.value = emptyList()
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun onQueryChanged(query: String){
        _searchQuery.value = query

        searchJob?.cancel()

        if(query == ""){
            loadTrendings()
        }
        else{
            searchJob = viewModelScope.launch {
                delay(1000)
                makeSearch(query)
            }
        }
    }

    fun addItemToWishlist(item: SearchResult){
        viewModelScope.launch {
            try {
                val fullItem =repository.getItemById(item.id)
                val added = repository.addToWishList(fullItem)
                if(added == true){
                    _events.emit(successfully_added)
                }
                else{
                    _events.emit(already_in_collection)
                }
            }
            catch (e: Exception){
                _events.emit(error)
                Log.d("addingToWishlist", e.message ?: "произошла ошибка")
            }
        }
    }
}