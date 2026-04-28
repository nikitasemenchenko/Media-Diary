package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.R

@Composable
fun FavouriteGenresSection(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    if (genres.isEmpty()) {
        Text(
            text = stringResource(R.string.no_genres),
            style = MaterialTheme.typography.bodyMedium
        )
        return
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.forEach { genre ->
            GenreRow(genre)

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray.copy(alpha = 0.35f)
            )
        }
    }
}

@Composable
private fun GenreRow(
    genre: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = genre.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}