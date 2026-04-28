package ru.magnum.mediadiary.presentation.search


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.presentation.components.AddToCollectionButton
import ru.magnum.mediadiary.presentation.components.ErrorState
import ru.magnum.mediadiary.presentation.components.MediaCard
import ru.magnum.mediadiary.presentation.components.MediaListCard
import ru.magnum.mediadiary.presentation.components.SearchField
import ru.magnum.mediadiary.presentation.components.SectionHeader

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun SearchScreen(
    vm: SearchViewModel = hiltViewModel(),
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onItemClick: (Int) -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    val searchResults by vm.searchResults.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(vm.events) {
        vm.events.collect { messageResId ->
            Toast.makeText(context, context.getString(messageResId), Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        SearchField(
            value = searchQuery,
            onValueChange = vm::onQueryChanged,
            onClearClick = vm::clearQuery
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    ErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = {
                            if (searchQuery.isBlank()) vm.loadTrending() else
                                vm.onQueryChanged(searchQuery)
                        }
                    )
                }

                searchQuery.isNotBlank() -> {
                    if (searchResults.isEmpty()) {
                        NoResults()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(searchResults,
                                key = { it.id }
                            ) { item ->
                                SearchResultCard(
                                    item = item,
                                    onAddToWishlist = vm::addItemToWishlist,
                                    onItemClick = onItemClick
                                )
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            CategoryCarousel(
                                title = R.string.trendingMovies,
                                list = uiState.trendingMovies,
                                onAddToWishlist = vm::addItemToWishlist,
                                onItemClick = onItemClick
                            )
                        }
                        item {
                            CategoryCarousel(
                                title = R.string.trendingSeries,
                                list = uiState.trendingSeries,
                                onAddToWishlist = vm::addItemToWishlist,
                                onItemClick = onItemClick
                            )
                        }
                        item {
                            CategoryCarousel(
                                title = R.string.trendingAnime,
                                list = uiState.trendingAnime,
                                onAddToWishlist = vm::addItemToWishlist,
                                onItemClick = onItemClick
                            )
                        }
                        item {
                            CategoryCarousel(
                                title = R.string.trendingCartoons,
                                list = uiState.trendingCartoons,
                                onAddToWishlist = vm::addItemToWishlist,
                                onItemClick = onItemClick
                            )
                        }
                        item {
                            CategoryCarousel(
                                title = R.string.trendingAnimatedSeries,
                                list = uiState.trendingAnimatedSeries,
                                onAddToWishlist = vm::addItemToWishlist,
                                onItemClick = onItemClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCarousel(
    title: Int,
    list: List<MediaPreview>,
    onAddToWishlist: (MediaPreview) -> Unit,
    onItemClick: (Int) -> Unit
) {
    if (list.isEmpty()) return

    SectionHeader(title = title)

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(232.dp),
        state = rememberLazyListState()
    ) {
        items(
            list,
            key = { it.id }
        ) { item ->
            MediaCard(
                title = item.title,
                posterUrl = item.poster,
                rating = item.rating,
                onAddClick = { onAddToWishlist(item) },
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

@Composable
fun SearchResultCard(
    item: MediaPreview,
    onAddToWishlist: (MediaPreview) -> Unit,
    onItemClick: (Int) -> Unit
) {
    MediaListCard(
        title = item.title,
        posterUrl = item.poster,
        rating = item.rating,
        type = item.type,
        year = item.year,
        genres = item.genres,
        onClick = { onItemClick(item.id) }
    ) {
        AddToCollectionButton(
            onClick = { onAddToWishlist(item) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
        )
    }
}

@Composable
fun NoResults() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.no_results),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.try_another_query),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}