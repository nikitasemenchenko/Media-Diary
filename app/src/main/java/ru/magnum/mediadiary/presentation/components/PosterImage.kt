package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun PosterImage(
    posterUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    width: Dp = 100.dp,
    height: Dp = 140.dp,
    contentScale: ContentScale = ContentScale.Crop,
    overlay: @Composable BoxScope.() -> Unit = {}
) {
    val context = LocalContext.current

    val request = remember(posterUrl) {
        ImageRequest.Builder(context)
            .data(posterUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .allowHardware(true)
            .crossfade(true)
            .build()
    }

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (posterUrl.isNullOrBlank()) {
            PosterPlaceholder(
                modifier = Modifier.matchParentSize()
            )
        }
        else {
            SubcomposeAsyncImage(
                model = request,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.matchParentSize(),
                loading = {
                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    PosterPlaceholder(
                        modifier = Modifier.matchParentSize()
                    )
                }
            )
        }

        overlay()
    }
}