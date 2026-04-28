package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.R

@Composable
fun StatsSummarySection(
    total: Int,
    watched: Int,
    watching: Int,
    wantToWatch: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatsDetailCard(
            title = R.string.total_in_collection,
            value = total.toString(),
            icon = Icons.Default.Favorite
        )

        StatsDetailCard(
            title = R.string.watched,
            value = watched.toString(),
            icon = Icons.Default.CheckCircle
        )

        StatsDetailCard(
            title = R.string.watching,
            value = watching.toString(),
            icon = Icons.Default.Visibility
        )

        StatsDetailCard(
            title = R.string.want_to_watch,
            value = wantToWatch.toString(),
            icon = Icons.Default.WatchLater
        )
    }
}