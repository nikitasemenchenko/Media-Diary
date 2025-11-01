package com.example.mediadiary.ui.search


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.mediadiary.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
                .padding(16.dp)
        ) {
            if (isLoading) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillParentMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(searchResults) { item ->
                    MediaItemCard(item = item,
                        onAddToWishlist = vm::addItemToWishlist,
                        onItemClick = onItemClick)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun MediaItemCard(item: SearchResult,
                  onAddToWishlist: (SearchResult) -> Unit,
                  onItemClick: (Int) -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = {onItemClick(item.id)})
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = item.getItemPoster(),
                contentDescription = item.getItemTitle(),
                modifier = Modifier.size(100.dp),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_connection_error)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.getItemTitle(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.getItemType(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = item.getItemYear(),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = item.getItemGenres(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = { onAddToWishlist(item) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

    }
}