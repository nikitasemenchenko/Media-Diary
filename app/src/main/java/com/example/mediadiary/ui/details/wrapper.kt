package com.example.mediadiary.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediadiary.ui.AppViewModelProvider

@Composable
fun MediaDetailsWrapper(vm: MediaDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
                        mediaId: Int, onBack: ()-> Unit){
    val mediaItem by vm.uiState.collectAsState()
    LaunchedEffect(mediaId) {
        vm.loadMediaItem(mediaId)
    }
    if(mediaItem != null){
        MediaDetailScreen(item = mediaItem!!,
            onBack = onBack,
            onStatusClick = {newStatus ->
                vm.updateStatus(newStatus)
                vm.createOrUpdateMediaItem()
            })
    }
}