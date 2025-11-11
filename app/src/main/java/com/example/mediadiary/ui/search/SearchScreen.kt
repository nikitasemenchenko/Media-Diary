package com.example.mediadiary.ui.search


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.SearchResult
import com.example.mediadiary.ui.AppViewModelProvider
import com.example.mediadiary.ui.navigation.NavigationDestination


object SearchDestination : NavigationDestination {
    override val route = "search"
}

@Composable
fun SearchScreen(
    vm: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onItemClick: (Int) -> Unit
) {
    val trendMovies by vm.trendingMovies.collectAsState()
    val trendSeries by vm.trendingSeries.collectAsState()
    val trendAnime by vm.trendingAnime.collectAsState()
    val trendCartoons by vm.trendingCartoon.collectAsState()
    val trendAnimatedSeries by vm.trendingAnimatedSeries.collectAsState()
    val searchResults by vm.searchResults.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(vm.events) {
        vm.events.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
            shape = MaterialTheme.shapes.medium
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(24.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (searchQuery.isNotBlank()) {
                items(searchResults) { item ->
                    SearchResultCard(
                        item = item,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick
                    )
                }
            } else {
                item {
                    CategoryCarousel(
                        title = R.string.trendingMovies,
                        list = trendMovies,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick
                    )
                }

                item {
                    CategoryCarousel(
                        title = R.string.trendingSeries,
                        list = trendSeries,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick
                    )
                }

                item {
                    CategoryCarousel(
                        title = R.string.trendingAnime,
                        list = trendAnime,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick
                    )
                }

                item {
                    CategoryCarousel(
                        title = R.string.trendingCartoons,
                        list = trendCartoons,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick
                    )
                }

                item {
                    CategoryCarousel(
                        title = R.string.trendingAnimatedSeries,
                        list = trendAnimatedSeries,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick
                    )
                    Spacer(modifier = Modifier.height(20.dp))
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
    if (list.isEmpty()) {
        return
    }
    SectionHeader(title = title)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(235.dp)
    ) {
        items(list.size) { index ->
            val item = list[index]
            MovieCard(
                title = item.getItemTitle(),
                posterUrl = item.getItemPoster(),
                rating = item.getItemRating(),
                onAdd = { onAddToWishlist(item) },
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

@Composable
fun MovieCard(
    title: String,
    posterUrl: String,
    rating: Double,
    onAdd: () -> Unit,
    onClick: () -> Unit
) {
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
                    model = posterUrl.takeIf { it.isNotBlank() },
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(185.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = rating.toString(),
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
                    text = title,
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
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 136.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = item.getItemPoster().takeIf { it.isNotBlank() },
                    contentDescription = item.getItemTitle(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)
                                ),
                                startY = 60f
                            )
                        )
                )

                Text(
                    text = item.getItemRating().toString(),
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
                    text = item.getItemTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = item.getItemType(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = item.getItemYear(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.getItemGenres(),
                    style = MaterialTheme.typography.bodySmall,
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