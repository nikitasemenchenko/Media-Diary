package ru.magnum.mediadiary.presentation.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.magnum.mediadiary.R

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
            LoadingScreen()
        }

        is MediaDetailUiState.Error -> {
            ErrorScreen(
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

@Composable
fun ErrorScreen(
    @StringRes message: Int,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(message),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
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