package ru.magnum.mediadiary.presentation.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.presentation.components.EmptyState
import ru.magnum.mediadiary.presentation.components.ErrorState
import ru.magnum.mediadiary.presentation.components.LoadingState
import ru.magnum.mediadiary.presentation.components.MediaListCard
import ru.magnum.mediadiary.presentation.mappers.titleRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    vm: CollectionViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onCollectionItemClick: (Int) -> Unit
) {
    val uiState by vm.uiState.collectAsState()

    if (uiState.isDeleteDialogVisible) {
        DeleteSelectedDialog(
            selectedCount = uiState.selectedItems.size,
            onConfirm = vm::confirmDeleteSelected,
            onDismiss = vm::dismissDeleteDialog
        )
    }

    Scaffold(
        topBar = {
            if (uiState.selectedItems.isNotEmpty()) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.selected_items) +
                                    ": ${uiState.selectedItems.size}"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = vm::clearSelection) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.cancel)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = vm::requestDeleteSelected) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                )
            }
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = scaffoldPadding.calculateTopPadding())
                .padding(bottom = contentPadding.calculateBottomPadding())
        ) {
            SecondaryTabRow(
                selectedTabIndex = WatchStatus.entries.indexOf(uiState.selectedTab)
            ) {
                WatchStatus.entries.forEach { status ->
                    Tab(
                        selected = status == uiState.selectedTab,
                        onClick = { vm.changeTab(status) },
                        text = {
                            Text(
                                text = stringResource(status.titleRes()),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            }

            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                uiState.errorMessage != null -> {
                    ErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = vm::loadItems
                    )
                }

                uiState.items.isEmpty() -> {
                    EmptyState(R.string.empty_collection)
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.items,
                            key = { it.id }
                        ) { item ->
                            CollectionItemCard(
                                item = item,
                                isSelected = uiState.selectedItems.contains(item.id),
                                isSelectionMode = uiState.selectedItems.isNotEmpty(),
                                onItemClick = onCollectionItemClick,
                                onToggleSelection = { vm.toggleDeletion(it) },
                                onLongClick = { vm.toggleDeletion(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionItemCard(
    item: MediaDetails,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onItemClick: (Int) -> Unit,
    onToggleSelection: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    MediaListCard(
        title = item.title,
        posterUrl = item.poster,
        rating = item.rating,
        type = item.type,
        year = item.year,
        genres = item.genres,
        isSelected = isSelected,
        onClick = {
            if (isSelectionMode) {
                onToggleSelection(item.id)
            } else {
                onItemClick(item.id)
            }
        },
        onLongClick = {
            onLongClick(item.id)
        }
    ) {
        if (isSelectionMode) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggleSelection(item.id) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
            )
        }
    }
}

@Composable
private fun DeleteSelectedDialog(
    selectedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete))
        },
        text = {
            Text(
                text = stringResource(
                    R.string.delete_selected_confirmation,
                    selectedCount
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}