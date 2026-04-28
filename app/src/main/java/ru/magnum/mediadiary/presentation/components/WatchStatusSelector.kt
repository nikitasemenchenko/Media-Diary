package ru.magnum.mediadiary.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.presentation.mappers.titleRes

@Composable
fun WatchStatusSelector(
    currentStatus: WatchStatus?,
    onStatusClick: (WatchStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WatchStatus.entries.forEach { status ->
            val icon = when (status) {
                WatchStatus.WANT_TO_WATCH -> Icons.Default.WatchLater
                WatchStatus.WATCHING -> Icons.Default.Visibility
                WatchStatus.WATCHED -> Icons.Default.CheckCircle
            }

            WatchStatusTab(
                icon = icon,
                label = status.titleRes(),
                selected = currentStatus == status,
                onClick = { onStatusClick(status) }
            )
        }
    }
}

@Composable
private fun WatchStatusTab(
    icon: ImageVector,
    @StringRes label: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(label),
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(2.dp)
                    .width(40.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}