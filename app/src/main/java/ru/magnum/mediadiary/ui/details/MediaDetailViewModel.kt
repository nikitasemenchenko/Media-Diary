package ru.magnum.mediadiary.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.data.remote.model.MediaItem
import ru.magnum.mediadiary.data.remote.model.MovieStatus
import ru.magnum.mediadiary.domain.repository.MediaRepository
import javax.inject.Inject

@HiltViewModel
class MediaDetailViewModel @Inject constructor(private val repository: MediaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadMediaItem(id: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = MediaDetailUiState.Loading

                val item = repository.getMediaItem(id)

                _uiState.value = MediaDetailUiState.Success(item)

            } catch (e: Exception) {
                _uiState.value = MediaDetailUiState.Error(R.string.error_loading)
                e.printStackTrace()
            }
        }
    }

    fun updateStatus(newStatus: MovieStatus) {
        val current = _uiState.value
        if (current !is MediaDetailUiState.Success) return

        val currentItem = current.item

        if (currentItem.watchStatus == newStatus) {
            viewModelScope.launch {
                repository.deleteMediaItem(currentItem)
                _uiState.value = MediaDetailUiState.Success(currentItem.copy(watchStatus = null))
            }
        } else {
            updateItem { it.copy(watchStatus = newStatus) }
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

    private fun updateItem(transform: (MediaItem) -> MediaItem) {
        val current = _uiState.value
        if (current !is MediaDetailUiState.Success) return

        val updated = transform(current.item)
        _uiState.value = MediaDetailUiState.Success(updated)

        viewModelScope.launch {
            repository.createOrUpdateItem(updated)
        }
    }


}