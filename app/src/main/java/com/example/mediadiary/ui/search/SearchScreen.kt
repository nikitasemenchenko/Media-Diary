package com.example.mediadiary.ui.search


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.SearchResult
import com.example.mediadiary.ui.AppViewModelProvider
import roundToOneSign


@Composable
fun SearchScreen(
    vm: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
        OutlinedTextField(
            value = searchQuery,
            onValueChange = vm::onQueryChanged,
            label = { Text(stringResource(R.string.search_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_label)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { vm.clearQuery() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear_query)
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            singleLine = true
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(uiState.errorMessage!!),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (searchQuery.isBlank()) vm.loadTrending() else
                                    vm.onQueryChanged(searchQuery)
                            }
                        ) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }

                searchQuery.isNotBlank() -> {
                    if (searchResults.isEmpty()) {
                        NoResults()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(searchResults, key = { it.id }) { item ->
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
                        contentPadding = PaddingValues(horizontal = 8.dp),
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
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: Int) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CategoryCarousel(
    title: Int,
    list: List<SearchResult>,
    onAddToWishlist: (SearchResult) -> Unit,
    onItemClick: (Int) -> Unit
) {
    if (list.isEmpty()) return

    SectionHeader(title = title)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(235.dp),
        state = rememberLazyListState()
    ) {
        items(
            list,
            key = { it.id },
            contentType = { "trending_item" }) { item ->
            MovieCard(
                title = item.getItemTitle(),
                posterUrl = item.poster?.url,
                rating = item.getItemRating(),
                onAdd = { onAddToWishlist(item) },
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

@Composable
fun MovieCard(
    title: String?,
    posterUrl: String?,
    rating: Double?,
    onAdd: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val request = remember(posterUrl) {
        ImageRequest.Builder(context)
            .data(posterUrl)
            .placeholder(R.drawable.loading_img)
            .error(R.drawable.ic_connection_error)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .allowHardware(true)
            .crossfade(true)
            .build()
    }

    Card(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Box {
                AsyncImage(
                    model = request,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(185.dp)
                        .fillMaxWidth()
                )
                val ratingText =
                    rating?.roundToOneSign()?.toString() ?: stringResource(R.string.unknown_value)
                Text(
                    text = ratingText,
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

                IconButton(
                    onClick = onAdd,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(22.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title ?: stringResource(R.string.unknown_title),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun SearchResultCard(
    item: SearchResult,
    onAddToWishlist: (SearchResult) -> Unit,
    onItemClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val request = remember(item.poster?.url) {
        ImageRequest.Builder(context)
            .data(item.poster?.url)
            .placeholder(R.drawable.loading_img)
            .error(R.drawable.ic_connection_error)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .allowHardware(true)
            .build()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onItemClick(item.id) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    )
    {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(150.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 140.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                AsyncImage(
                    model = request,
                    contentDescription = item.getItemTitle(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                val ratingText = item.getItemRating()?.roundToOneSign()?.toString()
                    ?: stringResource(R.string.unknown_value)

                Text(
                    text = ratingText,
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

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = item.getItemTitle() ?: stringResource(R.string.unknown_title),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = stringResource(item.getItemType().resId),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = item.year?.toString() ?: stringResource(R.string.unknown_year),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val genresText = item.getItemGenres().joinToString(", ").ifEmpty {
                    stringResource(R.string.unknown_value)
                }

                Text(
                    text = genresText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = { onAddToWishlist(item) },
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(35.dp),
                    tonalElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
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