package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.presentation.mappers.titleRes

@Composable
fun MediaTypeChip(
    type: MediaType?,
    modifier: Modifier = Modifier
) {
    if (type == null) return

    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        modifier = modifier
    ) {
        Text(
            text = stringResource(type.titleRes()),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}