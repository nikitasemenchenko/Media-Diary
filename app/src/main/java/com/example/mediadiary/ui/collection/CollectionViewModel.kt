package com.example.mediadiary.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediadiary.data.repository.MediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CollectionViewModel(private val repository: MediaRepository): ViewModel() {
    private val TIMEOUT_MILLIS = 5_000L
    val mediaItems = repository.getCollection().
        stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

}