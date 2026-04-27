package ru.magnum.mediadiary.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.magnum.mediadiary.MediaDiaryApplication
import ru.magnum.mediadiary.ui.collection.CollectionViewModel
import ru.magnum.mediadiary.ui.details.MediaDetailViewModel
import ru.magnum.mediadiary.ui.search.SearchViewModel
import ru.magnum.mediadiary.ui.statistics.StatisticsViewModel

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