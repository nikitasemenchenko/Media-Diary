package com.example.mediadiary.ui.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.ui.AppViewModelProvider
import com.example.mediadiary.ui.navigation.NavigationDestination


object CollectionDestination : NavigationDestination {
    override val route = "collection"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    vm: CollectionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onCollectionItemClick: (Int) -> Unit
) {
    val items by vm.mediaItems.collectAsState()
    val selectedTab by vm.selectedTab.collectAsState()
    val selectedItems by vm.selectedItems.collectAsState()
    Scaffold(
        topBar = {
            if (selectedItems.isNotEmpty()) {
                TopAppBar(
                    title = { Text("Выбрано: ${selectedItems.size}") },
                    navigationIcon = {
                        IconButton(onClick = { vm.clearSelection() }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.cancel)
                            )
                        }
                    },
                    actions = {
                        if (selectedItems.isNotEmpty()) {
                            IconButton(onClick = { vm.deleteSelected() }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            TabRow(
                selectedTabIndex = MovieStatus.entries.indexOf(selectedTab)
            ) {
                MovieStatus.entries.forEach { status ->
                    Tab(
                        selected = status == selectedTab,
                        onClick = { vm.selectTab(status) },
                        text = {
                            Text(
                                text = status.statusName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            }
            if (items.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    Text(
                        text = stringResource(R.string.empty_collection)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        CollectionItemCard(
                            item = item,
                            isSelected = selectedItems.contains(item.id),
                            isSelectionMode = selectedItems.isNotEmpty(),
                            onItemClick = onCollectionItemClick,
                            onToggleSelection = { vm.toggleSelection(it) },
                            onLongClick = { vm.toggleSelection(it) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionItemCard(
    item: MediaItem,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onItemClick: (Int) -> Unit,
    onToggleSelection: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        onToggleSelection(item.id)
                    } else {
                        onItemClick(item.id)
                    }
                },
                onLongClick = { onLongClick(item.id) }
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PosterSection(item)

                Spacer(Modifier.width(12.dp))

                InfoSection(item)
            }

            if (isSelected) {
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                        )
                )
            }

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
}

@Composable
fun PosterSection(item: MediaItem) {
    Box(
        modifier = Modifier
            .size(width = 96.dp, height = 136.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        AsyncImage(
            model = item.poster,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)
                        ),
                        startY = 60f
                    )
                )
        )

        Text(
            text = item.rating.toString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun InfoSection(item: MediaItem) {
    Column(
        modifier = Modifier
            .padding(end = 8.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            ) {
                Text(
                    text = item.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Text(
                text = item.year,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = item.genres,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

