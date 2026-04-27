package ru.magnum.mediadiary.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.domain.repository.MediaRepository
import ru.magnum.mediadiary.presentation.mappers.toMessageRes
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModel @Inject constructor(
    private val repository: MediaRepository,
) : ViewModel() {

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
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toMessageRes()
                        )
                    }
                }
                .collect { filteredItems ->
                    _uiState.update {
                        it.copy(items = filteredItems, isLoading = false)
                    }
                }
        }
    }


    fun changeTab(tab: WatchStatus) {
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
            runCatching {
                repository.deleteItemsByIds(toDelete)
                clearSelection()
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.toMessageRes()
                    )
                }
            }
        }
    }
}