package com.example.mediadiary.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.AppConstants.TIMEOUT_MILLIS
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CollectionViewModel(private val repository: MediaRepository) : ViewModel() {
    private val _selectedItems = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItems = _selectedItems.asStateFlow()

    private val _selectedTab = MutableStateFlow(MovieStatus.WANT_TO_WATCH)
    val selectedTab = _selectedTab.asStateFlow()
    val mediaItems = combine(
        repository.getCollection(),
        selectedTab
    ) { items, status ->
        items.filter { it.watchStatus == status }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    fun selectTab(status: MovieStatus) {
        _selectedTab.value = status
    }

    fun toggleSelection(id: Int) {
        _selectedItems.value = _selectedItems.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
        }.toSet()
    }

    fun clearSelection() {
        _selectedItems.value = emptySet()
    }

    fun deleteSelected() {
        val toDelete = _selectedItems.value.toList()
        if (toDelete.isEmpty()) return
        viewModelScope.launch {
            toDelete.forEach { id ->
                val item = repository.getOrCreateMediaItem(id)
                repository.deleteMediaItem(item)
            }
            clearSelection()
        }
    }
}