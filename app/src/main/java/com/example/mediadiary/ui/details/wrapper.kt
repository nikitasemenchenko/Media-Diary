package com.example.mediadiary.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediadiary.data.AppConstants.loading_eroor
import com.example.mediadiary.ui.AppViewModelProvider

@Composable
fun MediaDetailsWrapper(
    vm: MediaDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    mediaId: Int, onBack: () -> Unit
) {
    val mediaItem by vm.uiState.collectAsState()
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(mediaId) {
        try {
            vm.loadMediaItem(mediaId)
        } catch (e: Exception) {
            error = loading_eroor
        }
    }
    error?.let { ErrorMessage(it) }
    if (mediaItem != null) {
        MediaDetailScreen(
            item = mediaItem!!,
            onBack = onBack,
            onStatusClick = { newStatus ->
                vm.updateStatus(newStatus)
                vm.createOrUpdateMediaItem()
            },
            onRatingChange = { newRating ->
                vm.updateRating(newRating)
                vm.createOrUpdateMediaItem()
            },
            onDateChange = { date ->
                vm.updateDate(date)
                vm.createOrUpdateMediaItem()
            },
            onNoteChange = { note ->
                vm.updateNote(note)
                vm.createOrUpdateMediaItem()
            }
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}