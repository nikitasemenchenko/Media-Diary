package com.example.mediadiary.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModel(private val repository: MediaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _uiState
                .map { it.selectedTab }
                .distinctUntilChanged()
                .flatMapLatest { status ->
                    _uiState.update { it.copy(isLoading = true) }
                    repository.getCollectionByStatus(status)
                }
                .collect { filteredItems ->
                    _uiState.update {
                        it.copy(items = filteredItems, isLoading = false)
                    }
                }
        }
    }


    fun changeTab(tab: MovieStatus) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun toggleDeletion(id: Int) {
        _uiState.update {
            val updated = it.selectedItems.toMutableSet()
            if (updated.contains(id)) updated.remove(id) else updated.add(id)
            it.copy(selectedItems = updated)
        }
    }


    fun clearSelection() {
        _uiState.update {
            it.copy(selectedItems = emptySet())
        }
    }

    fun deleteSelected() {
        val toDelete = _uiState.value.selectedItems.toList()
        if (toDelete.isEmpty()) return
        viewModelScope.launch {
            repository.deleteItemsByIds(toDelete)
            clearSelection()
        }
    }
}