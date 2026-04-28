package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import roundToOneSign
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.presentation.theme.gold
import ru.magnum.mediadiary.presentation.theme.green

@Composable
fun DetailsPosterImage(
    item: MediaDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val request = remember(item.poster) {
        ImageRequest.Builder(context)
            .data(item.poster)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .allowHardware(true)
            .crossfade(true)
            .build()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(520.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (item.poster.isNullOrBlank()) {
            PosterPlaceholder()
        } else {
            SubcomposeAsyncImage(
                model = request,
                contentDescription = item.title,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                loading = {
                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    PosterPlaceholder(
                        modifier = Modifier.matchParentSize()
                    )
                }
            )
        }


        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                        ),
                        startY = 120f
                    )
                )
        )

        item.rating?.let { rating ->
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = ratingColor(rating),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Text(
                    text = rating.roundToOneSign().toString(),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }

        Text(
            text = item.title?.takeIf { it.isNotBlank() }
                ?: stringResource(R.string.unknown_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ratingColor(rating: Double): Color {
    return when {
        rating >= 8.0 -> gold
        rating >= 5.5 -> green
        else -> MaterialTheme.colorScheme.error
    }
}