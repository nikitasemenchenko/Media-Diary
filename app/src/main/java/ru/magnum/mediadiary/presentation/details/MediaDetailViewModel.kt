package ru.magnum.mediadiary.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.domain.repository.MediaRepository
import ru.magnum.mediadiary.presentation.mappers.toMessageRes
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val repository: MediaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadMediaItem(id: Int) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = MediaDetailUiState.Loading

                val item = repository.getMediaDetails(id)

                _uiState.value = MediaDetailUiState.Success(item)

            }.onFailure { e ->
                if (e is CancellationException) throw e
                _uiState.value = MediaDetailUiState.Error(e.toMessageRes())
            }
        }
    }

    fun updateStatus(newStatus: WatchStatus) {
        val current = _uiState.value
        if (current !is MediaDetailUiState.Success) return

        val currentItem = current.item

        if (currentItem.watchStatus == newStatus) {
            viewModelScope.launch {
                runCatching {
                    repository.deleteMediaDetails(currentItem)
                    _uiState.value = MediaDetailUiState.Success(currentItem.copy(watchStatus = null))
                }.onFailure { e ->
                    if (e is CancellationException) throw e
                    _uiState.value = MediaDetailUiState.Error(e.toMessageRes())
                }
            }
        } else {
            updateItem {
                it.copy(watchStatus = newStatus) }
        }
    }

    fun updateRating(newRating: Int?) {
        updateItem { it.copy(userRating = newRating) }
    }

    fun updateNote(note: String?) {
        updateItem { it.copy(userNote = note) }
    }

    fun updateDate(date: Long?) {
        updateItem { it.copy(watchDate = date) }
    }

    private fun updateItem(transform: (MediaDetails) -> MediaDetails) {
        val current = _uiState.value
        if (current !is MediaDetailUiState.Success) return

        val updated = transform(current.item)
        _uiState.value = MediaDetailUiState.Success(updated)

        viewModelScope.launch {
            runCatching {
                repository.saveMediaDetails(updated)
            }.onFailure { e ->
                if (e is CancellationException) throw e
                _uiState.value = MediaDetailUiState.Error(e.toMessageRes())
            }
        }
    }


}