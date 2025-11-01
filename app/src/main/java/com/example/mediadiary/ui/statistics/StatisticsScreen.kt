package com.example.mediadiary.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.ui.AppViewModelProvider
import com.example.mediadiary.ui.navigation.NavigationDestination

object StatisticsDestination : NavigationDestination {
    override val route = "statistics"
}

@Composable
fun StatisticsScreen(
    vm: StatisticsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val stats by vm.uiState.collectAsState()

    if (stats == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatsCard(title = MovieStatus.WANT_TO_WATCH.statusName, value = stats!!.wantToWatch.toString())
            StatsCard(title = MovieStatus.WATCHING.statusName, value = stats!!.watching.toString())
            StatsCard(title = MovieStatus.WATCHED.statusName, value = stats!!.watched.toString())
        }
    }
}

@Composable
fun StatsCard(title: String, value: String){
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}