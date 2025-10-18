package com.example.mediadiary.ui.search

import android.R.attr.mode
import android.R.attr.text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mediadiary.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mediadiary.data.remote.model.SearchResult

@Composable
fun SearchScreen(vm: SearchViewModel,
                 modifier: Modifier = Modifier){
    val searchResults by vm.searchResults.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = vm::onQueryChanged,
            label = {
                Text(
                    text = stringResource(R.string.search_label)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(16.dp))
        if(isLoading){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
            ) {
                items(searchResults){ item ->
                    MediaItemCard(item)
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Composable
fun MediaItemCard(item: SearchResult){
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ){
            AsyncImage(
                model = item.getItemPoster(),
                contentDescription = item.getItemTitle(),
                modifier = Modifier.size(100.dp)
            )
            Column {
                Text(
                    text = item.getItemTitle(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if(item.isMovie()) stringResource(R.string.movie) else stringResource(R.string.series),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = item.getItemYear(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }
}