package com.example.mediadiary.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.remote.model.MediaStats
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: MediaRepository): ViewModel(){
    private val _uiState = MutableStateFlow<MediaStats?>(null)
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val stats = repository.getMediaStats()
            _uiState.value = stats
        }
    }
}