package ru.magnum.mediadiary.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaDetails

@Composable
fun DetailsInfoSection(
    item: MediaDetails,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxWidth()
    ) {
        InfoRow(R.string.year, item.year?.toString())
        InfoRow(R.string.country, item.countries)
        InfoRow(R.string.director, item.director)
        InfoRow(R.string.duration, item.length)
        InfoRow(R.string.age, item.ageRating)
        InfoRow(R.string.actors, item.actors)
    }
}

@Composable
fun DetailsGenreSection(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    val visibleGenres = genres.filter { it.isNotBlank() }

    if (visibleGenres.isEmpty()) return

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        visibleGenres.forEach { genre ->
            GenreChip(genre)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun InfoRow(
    @StringRes label: Int,
    value: String?
) {
    if (value.isNullOrBlank()) return

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold)
            ) {
                append(stringResource(label))
                append(" ")
            }

            append(value)
        },
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

@Composable
private fun GenreChip(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}