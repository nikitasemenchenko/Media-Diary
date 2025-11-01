package com.example.mediadiary.ui.collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
@Composable
fun CollectionsScreen(
    vm: CollectionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues = PaddingValues(),
    onCollectionItemClick: (Int) -> Unit
) {
    val items by vm.mediaItems.collectAsState()
    val selectedTab by vm.selectedTab.collectAsState()

    Column {
        TabRow(
            selectedTabIndex = MovieStatus.entries.indexOf(selectedTab),
            modifier = Modifier.statusBarsPadding()
        ) {
            MovieStatus.entries.forEach { status ->
                Tab(
                    selected = status == selectedTab,
                    onClick = {vm.selectTab(status)},
                    text = {Text(status.statusName)}
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
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    CollectionItemCard(item, onCollectionItemClick)
                }
            }
        }
    }
}

@Composable
fun CollectionItemCard(item: MediaItem,
                       onCollectionItemClick: (Int) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onCollectionItemClick(item.id) })
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = item.poster,
                contentDescription = item.title,
                modifier = Modifier.size(100.dp),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_connection_error)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = item.year,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = item.genres,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }
}