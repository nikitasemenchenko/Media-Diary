package com.example.mediadiary.ui.details

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.data.roundToOneSign
import com.example.mediadiary.ui.AppViewModelProvider
import com.example.mediadiary.ui.navigation.NavigationDestination
import com.example.mediadiary.ui.theme.gold
import com.example.mediadiary.ui.theme.green
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MediaDetailDestination : NavigationDestination {
    const val MEDIA_ID = "mediaId"
    override val route = "media_detail/{$MEDIA_ID}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    vm: MediaDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    item: MediaItem,
    onStatusClick: (MovieStatus) -> Unit,
    onRatingChange: (Int) -> Unit,
    onDateChange: (Long?) -> Unit,
    onNoteChange: (String?) -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            PosterHeader(
                posterUrl = item.poster,
                title = item.title,
                ratingText = item.rating.roundToOneSign(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Info grid
            InfoRow(R.string.year, item.year)
            InfoRow(R.string.country, item.countries)
            InfoRow(R.string.director, item.director)
            InfoRow(R.string.duration, item.length)
            InfoRow(R.string.age, item.ageRating)
            InfoRow(R.string.actors, item.actors)

            Spacer(modifier = Modifier.height(8.dp))

            if (item.genres.isNotBlank()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item.genres.split(",").map { it.trim() }.take(6).forEach { g ->
                        GenreChip(g)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (!item.description.isNullOrBlank()) {
                ExpandableText(
                    text = item.description,
                    initiallyExpanded = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = stringResource(R.string.status),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            StatusSelector(currentStatus = item.watchStatus, onStatusClick)

            if (item.watchStatus == MovieStatus.WATCHED) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.user_rating),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                StarRatingSelector(rating = item.userRating, onRatingChanged = onRatingChange)

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.select_date),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                WatchDateSelector(
                    watchDate = item.watchDate ?: item.addedAt,
                    onDateChanged = onDateChange
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.note),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = item.userNote ?: "",
                    onValueChange = { onNoteChange(it.ifEmpty { null }) },
                    label = { Text(stringResource(R.string.note_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    minLines = 2,
                    shape = MaterialTheme.shapes.medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatusSelector(currentStatus: MovieStatus?, onStatusClick: (MovieStatus) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MovieStatus.entries.forEach { status ->
            StatusTab(
                text = status.statusName,
                selected = currentStatus == status,
                onClick = { onStatusClick(status) })
        }
    }
}

@Composable
fun StatusTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(70.dp)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(0.7f),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(3.dp)
                    .width(40.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun InfoRow(@StringRes label: Int, value: String?) {
    if (value.isNullOrBlank() || value == "-") return
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
fun StarRatingSelector(rating: Int?, onRatingChanged: (Int) -> Unit) {
    var selectedRating by remember { mutableIntStateOf(rating ?: 0) }
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        for (i in 1..10) {
            Icon(
                imageVector = if (i <= selectedRating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = selectedRating.toString(),
                tint = if (i <= selectedRating) Color.Yellow else Color.DarkGray,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        onClick = {
                            selectedRating = i
                            onRatingChanged(selectedRating)
                        },
                        indication = null,
                        interactionSource = interactionSource
                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchDateSelector(
    watchDate: Long?,
    onDateChanged: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = watchDate ?: System.currentTimeMillis()
    )
    OutlinedTextField(
        value = if (watchDate != null) {
            convertMillisToDate(watchDate)
        } else {
            "Дата не выбрана"
        },
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            IconButton(
                onClick = { showDialog = true }) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date)
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(vertical = 8.dp)
    )
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    onDateChanged(datePickerState.selectedDateMillis)
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Отмена") } },
            content = {
                DatePicker(state = datePickerState)
            })
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun PosterHeader(
    posterUrl: String?,
    title: String,
    ratingText: Double
) {
    @Composable
    fun ratingColor(rating: Double): Color {
        return when {
            rating >= 8.0 -> gold
            rating >= 4.5 -> green
            else -> MaterialTheme.colorScheme.error
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = posterUrl,
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        ),
                        startY = 160f
                    )
                )
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = ratingColor(ratingText),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Text(
                text = ratingText.toString(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    initiallyExpanded: Boolean = false,
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val maxLines = if (expanded) Int.MAX_VALUE else 4
    var isOverflowing by remember { mutableStateOf(false) }
    var checkNeeded by remember { mutableStateOf(true) }
    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (checkNeeded) {
                    isOverflowing = textLayoutResult.hasVisualOverflow
                    checkNeeded = false
                }

            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        if (isOverflowing) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(
                    text = if (expanded) stringResource(R.string.show_less)
                    else stringResource(R.string.show_more)
                )
            }
        }
    }
}

@Composable
fun GenreChip(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .sizeIn(minHeight = 32.dp)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}