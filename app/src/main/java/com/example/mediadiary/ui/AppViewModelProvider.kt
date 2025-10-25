package com.example.mediadiary.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediadiary.MediaDiaryApplication
import com.example.mediadiary.ui.collection.CollectionViewModel
import com.example.mediadiary.ui.search.SearchViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchViewModel(
                inventoryApplication().container.mediaRepository
            )
        }
        initializer {
            CollectionViewModel(inventoryApplication().container.mediaRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): MediaDiaryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MediaDiaryApplication)