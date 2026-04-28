package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.R

@Composable
fun MediaCard(
    title: String?,
    posterUrl: String?,
    rating: Double?,
    onAddClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(128.dp)
            .fillMaxHeight()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                PosterImage(
                    posterUrl = posterUrl,
                    contentDescription = title,
                    width = 128.dp,
                    height = 180.dp,
                    contentScale = ContentScale.Crop
                ) {
                    Rating(
                        rating = rating,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    )

                    AddToCollectionButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp),
                        size = 30.dp
                    )
                }
            }

            Text(
                text = title?.takeIf { it.isNotBlank() } ?: stringResource(R.string.unknown_title),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
    }
}