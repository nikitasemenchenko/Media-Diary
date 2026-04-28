package ru.magnum.mediadiary.presentation.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.magnum.mediadiary.presentation.components.ErrorState
import ru.magnum.mediadiary.presentation.components.LoadingState

@Composable
fun MediaDetailsWrapper(
    vm: MediaDetailViewModel = hiltViewModel(),
    mediaId: Int,
    onBack: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(mediaId) {
        vm.loadMediaItem(mediaId)
    }

    when (uiState) {

        is MediaDetailUiState.Loading -> {
            LoadingState()
        }

        is MediaDetailUiState.Error -> {
            ErrorState(
                message = (uiState as MediaDetailUiState.Error).message,
                onRetry = { vm.loadMediaItem(mediaId) }
            )
        }

        is MediaDetailUiState.Success -> {
            val item = (uiState as MediaDetailUiState.Success).item

            MediaDetailScreen(
                item = item,
                onBack = onBack,
                onStatusClick = vm::updateStatus,
                onRatingChange = vm::updateRating,
                onDateChange = vm::updateDate,
                onNoteChange = vm::updateNote
            )
        }
    }
}
