package ru.magnum.mediadiary.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.mediadiary.domain.repository.MediaRepository
import ru.magnum.mediadiary.presentation.mappers.toMessageRes
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: MediaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
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
            }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toMessageRes()
                        )
                    }
                }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}