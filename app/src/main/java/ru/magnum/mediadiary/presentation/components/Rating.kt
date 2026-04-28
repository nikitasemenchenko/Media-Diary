package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import roundToOneSign
import ru.magnum.mediadiary.R

@Composable
fun Rating(
    rating: Double?,
    modifier: Modifier = Modifier
) {
    val ratingText = rating?.roundToOneSign()?.toString()
        ?: stringResource(R.string.unknown_value)

    Text(
        text = ratingText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
    )
}