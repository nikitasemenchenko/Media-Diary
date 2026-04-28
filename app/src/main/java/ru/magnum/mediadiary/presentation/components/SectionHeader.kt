package ru.magnum.mediadiary.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = androidx.compose.ui.res.stringResource(title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 14.dp, bottom = 8.dp)
    )
}