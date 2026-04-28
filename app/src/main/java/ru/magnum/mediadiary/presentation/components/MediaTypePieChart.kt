package ru.magnum.mediadiary.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.presentation.mappers.pluralTitleRes
import ru.magnum.mediadiary.presentation.theme.pieChartColor1
import ru.magnum.mediadiary.presentation.theme.pieChartColor2
import ru.magnum.mediadiary.presentation.theme.pieChartColor3
import ru.magnum.mediadiary.presentation.theme.pieChartColor4
import ru.magnum.mediadiary.presentation.theme.pieChartColor5
import kotlin.math.min

@Composable
fun MediaTypePieChart(
    types: Map<MediaType, Int>,
    modifier: Modifier = Modifier
) {
    if (types.isEmpty()) {
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.bodyLarge
        )
        return
    }

    val colors = listOf(
        pieChartColor1,
        pieChartColor2,
        pieChartColor3,
        pieChartColor4,
        pieChartColor5
    )

    val total = types.values.sum()
    if (total == 0) {
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.bodyLarge
        )
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(150.dp)) {
            var startAngle = -90f
            val radius = min(size.width, size.height) / 2f

            types.entries.forEachIndexed { index, entry ->
                val sweep = entry.value.toFloat() / total * 360f

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    topLeft = Offset(
                        x = size.width / 2 - radius,
                        y = size.height / 2 - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )

                startAngle += sweep
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            types.entries.forEachIndexed { index, entry ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(colors[index % colors.size])
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = stringResource(entry.key.pluralTitleRes()),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}