package ru.magnum.mediadiary.presentation.collection

import androidx.annotation.StringRes
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.WatchStatus

data class CollectionUiState(
    val selectedTab: WatchStatus = WatchStatus.WANT_TO_WATCH,
    val selectedItems: Set<Int> = emptySet(),
    val items: List<MediaDetails> = emptyList(),
    val isLoading: Boolean = true,
    @StringRes val errorMessage: Int? = null,
    val isDeleteDialogVisible: Boolean = false
)