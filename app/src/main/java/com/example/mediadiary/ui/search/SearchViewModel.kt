package com.example.mediadiary.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediadiary.MediaDiaryApplication
import com.example.mediadiary.data.remote.model.SearchResult
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress

class SearchViewModel(private val repository: MediaRepository): ViewModel() {
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

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
                delay(800)
                makeSearch(query)
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =(this[APPLICATION_KEY] as MediaDiaryApplication)
                val repository = application.container.mediaRepository
                SearchViewModel(repository = repository )
            }
        }
    }
}