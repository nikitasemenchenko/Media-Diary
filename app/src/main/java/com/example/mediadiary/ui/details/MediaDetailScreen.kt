package com.example.mediadiary.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.ui.AppViewModelProvider
import com.example.mediadiary.ui.navigation.NavigationDestination

object MediaDetailDestination : NavigationDestination {
    const val MEDIA_ID= "mediaId"
    override val route = "media_detail/{$MEDIA_ID}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    vm: MediaDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    item: MediaItem,
    onStatusClick: (MovieStatus) -> Unit,
    onBack: () -> Unit
){
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)

        ) {
                AsyncImage(
                    model = item.poster,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize()
                )
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.rating.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = if(item.isMovie() || item.isCartoon()) "О фильме" else "О сериале",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Год: ${item.year}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Страна: ${item.countries}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Жанр: ${item.genres}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Режиссер: ${item.director}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Актерский состав: ${item.actors}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Длительность: ${item.length} мин.",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Возрастные ограничения: ${item.ageRating}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                }
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = item.description ?: stringResource(R.string.no_description),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Статус",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            StatusSelector(currentStatus =  item.watchStatus, onStatusClick)


            Spacer(modifier = Modifier.padding(16.dp))
        }

    }
}

@Composable
fun StatusSelector(currentStatus: MovieStatus?,
                   onStatusClick: (MovieStatus)-> Unit){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        MovieStatus.entries.forEach { status ->
            FilterChip(
                selected = currentStatus == status,
                label = {Text(status.statusName)},
                onClick = {onStatusClick(status)}
            )
        }
    }
}