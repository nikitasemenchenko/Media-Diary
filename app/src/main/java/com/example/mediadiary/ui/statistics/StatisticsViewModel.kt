package com.example.mediadiary.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: MediaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                repository.getCollectionStats(),
                repository.getTypesCount(),
                repository.getTopGenres()
            ) { stats, types, genres ->
                StatisticsUiState(
                    total = stats.total,
                    watched = stats.watched,
                    watching = stats.watching,
                    wantToWatch = stats.wantToWatch,
                    types = types,
                    topGenres = genres,
                    isLoading = false,
                    errorMessage = null
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}