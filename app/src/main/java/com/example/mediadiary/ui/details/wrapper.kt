package com.example.mediadiary.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediadiary.ui.AppViewModelProvider

@Composable
fun MediaDetailsWrapper(
    vm: MediaDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    mediaId: Int,
    onBack: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(mediaId) {
        vm.loadMediaItem(mediaId)
    }

    when (uiState) {

        is MediaDetailUiState.Loading -> {
            LoadingScreen()
        }

        is MediaDetailUiState.Error -> {
            ErrorScreen((uiState as MediaDetailUiState.Error).message)
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

@Composable
fun ErrorScreen(message: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(message),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}