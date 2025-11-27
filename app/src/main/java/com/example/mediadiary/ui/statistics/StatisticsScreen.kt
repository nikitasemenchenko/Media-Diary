package com.example.mediadiary.ui.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.ContentType
import com.example.mediadiary.ui.AppViewModelProvider
import com.example.mediadiary.ui.theme.pieChartColor1
import com.example.mediadiary.ui.theme.pieChartColor2
import com.example.mediadiary.ui.theme.pieChartColor3
import com.example.mediadiary.ui.theme.pieChartColor4
import com.example.mediadiary.ui.theme.pieChartColor5
import kotlin.math.min

@Composable
fun StatisticsScreen(
    vm: StatisticsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.statistics),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                StatsDetailCard(
                    title = R.string.total_in_collection,
                    value = uiState.total.toString(),
                    icon = Icons.Default.Favorite
                )
                StatsDetailCard(
                    title = R.string.watched,
                    value = uiState.watched.toString(),
                    icon = Icons.Default.CheckCircle
                )
                StatsDetailCard(
                    title = R.string.watching,
                    value = uiState.watching.toString(),
                    icon = Icons.Default.Visibility
                )
                StatsDetailCard(
                    title = R.string.want_to_watch,
                    value = uiState.wantToWatch.toString(),
                    icon = Icons.Default.WatchLater
                )
            }
        }

        item {
            Text(
                stringResource(R.string.raspredelenie),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Diagram(
                uiState.types, Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        item {
            Text(
                stringResource(R.string.favourite_genres),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            if (uiState.topGenres.isEmpty()) {
                Text(
                    stringResource(R.string.no_genres),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.topGenres.forEach { genre ->
                        GenreRow(genre)
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun StatsDetailCard(title: Int, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun GenreRow(genre: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            genre.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun Diagram(types: Map<ContentType, Int>, modifier: Modifier = Modifier) {
    if (types.isEmpty()) {
        Text(stringResource(R.string.no_data), style = MaterialTheme.typography.bodyLarge)
        return
    }

    val colors = listOf(
        pieChartColor1,
        pieChartColor2,
        pieChartColor3,
        pieChartColor4,
        pieChartColor5
    )

    val total = types.values.sum()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Canvas(modifier = Modifier.size(150.dp)) {
            var startAngle = -90f
            val radius = min(size.width, size.height) / 2f

            types.entries.forEachIndexed { index, entry ->
                val sweep = (entry.value.toFloat() / total) * 360f

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    topLeft = Offset(size.width / 2 - radius, size.height / 2 - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )

                startAngle += sweep
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            types.entries.forEachIndexed { index, (contentType, _) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(colors[index % colors.size])
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(contentType.pluralResId))
                }
            }
        }
    }
}
