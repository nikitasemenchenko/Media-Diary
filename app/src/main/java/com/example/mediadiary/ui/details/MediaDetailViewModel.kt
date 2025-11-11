package com.example.mediadiary.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel(private val repository: MediaRepository) : ViewModel() {
    val _uiState = MutableStateFlow<MediaItem?>(null)
    val uiState = _uiState.asStateFlow()

    suspend fun loadMediaItem(id: Int) {
        _uiState.value = repository.getOrCreateMediaItem(id)
    }

    fun updateStatus(newStatus: MovieStatus) {
        _uiState.value = _uiState.value?.copy(watchStatus = newStatus)
    }

    fun updateRating(newRating: Int?) {
        _uiState.value = _uiState.value?.copy(userRating = newRating)
    }

    fun updateNote(note: String?) {
        _uiState.value = _uiState.value?.copy(userNote = note)
    }

    fun updateDate(date: Long?) {
        _uiState.value = _uiState.value?.copy(watchDate = date)
    }

    fun createOrUpdateMediaItem() {
        val item = _uiState.value ?: return
        viewModelScope.launch {
            repository.createOrUpdateItem(item)
        }
    }

}