package com.example.mediadiary.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CollectionViewModel(private val repository: MediaRepository): ViewModel() {
    private val _selectedTab = MutableStateFlow<MovieStatus>(MovieStatus.WANT_TO_WATCH)
    val selectedTab = _selectedTab.asStateFlow()

    private val TIMEOUT_MILLIS = 5_000L
    val mediaItems = combine(
        repository.getCollection(),
        selectedTab
    ){ items, status ->
        items.filter { it.watchStatus == status }

    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )
    fun selectTab(status: MovieStatus){
        _selectedTab.value = status
    }
}