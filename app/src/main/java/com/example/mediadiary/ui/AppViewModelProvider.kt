package com.example.mediadiary.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediadiary.MediaDiaryApplication
import com.example.mediadiary.ui.collection.CollectionViewModel
import com.example.mediadiary.ui.details.MediaDetailViewModel
import com.example.mediadiary.ui.search.SearchViewModel
import com.example.mediadiary.ui.statistics.StatisticsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchViewModel(mediaDiaryApplication().container.mediaRepository)
        }
        initializer {
            CollectionViewModel(mediaDiaryApplication().container.mediaRepository)
        }
        initializer {
            MediaDetailViewModel(mediaDiaryApplication().container.mediaRepository)
        }
        initializer {
            StatisticsViewModel(mediaDiaryApplication().container.mediaRepository)
        }
    }
}

fun CreationExtras.mediaDiaryApplication(): MediaDiaryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MediaDiaryApplication)