package ru.magnum.mediadiary.presentation.statistics

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.presentation.components.EmptyState
import ru.magnum.mediadiary.presentation.components.ErrorState
import ru.magnum.mediadiary.presentation.components.FavouriteGenresSection
import ru.magnum.mediadiary.presentation.components.LoadingState
import ru.magnum.mediadiary.presentation.components.MediaTypePieChart
import ru.magnum.mediadiary.presentation.components.StatsSummarySection

@Composable
fun StatisticsScreen(
    vm: StatisticsViewModel = hiltViewModel(),
    contentPadding: PaddingValues
) {
    val uiState by vm.uiState.collectAsState()
    when {
        uiState.isLoading -> {
            LoadingState()
        }
        uiState.errorMessage != null -> {
            ErrorState(
                message = uiState.errorMessage!!,
                onRetry = vm::loadStatistics
            )
        }
        uiState.total == 0 -> {
            EmptyState(
                message = R.string.no_data
            )
        }
        else -> {
            StatisticsContent(
                uiState,
                contentPadding
            )
        }
    }
}

@Composable
fun StatisticsContent(
    uiState: StatisticsUiState,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.statistics),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        item {
            StatsSummarySection(
                total = uiState.total,
                watched = uiState.watched,
                watching = uiState.watching,
                wantToWatch = uiState.wantToWatch
            )
        }

        item {
            Column {
                StatsTitle(R.string.raspredelenie)
                MediaTypePieChart(
                    types = uiState.types,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
        item {
            Column {
                StatsTitle(R.string.favourite_genres)
                FavouriteGenresSection(
                    genres = uiState.topGenres
                )
            }
        }
    }
}

@Composable
fun StatsTitle(
    @StringRes title: Int
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}
