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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.mediadiary.R
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus
import com.example.mediadiary.ui.theme.gold
import com.example.mediadiary.ui.theme.green
import roundToOneSign
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    item: MediaItem,
    onStatusClick: (MovieStatus) -> Unit,
    onRatingChange: (Int) -> Unit,
    onDateChange: (Long?) -> Unit,
    onNoteChange: (String?) -> Unit,
    onBack: () -> Unit
) {
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PosterHeader(item)

            MediaInfoSection(item)

            GenreSection(item)

            DescriptionSection(item)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Title(R.string.status)

            StatusSelector(currentStatus = item.watchStatus, onStatusClick)

            if (item.watchStatus == MovieStatus.WATCHED) {
                Title(R.string.user_rating)

                StarRatingSelector(rating = item.userRating, onRatingChanged = onRatingChange)

                Title(R.string.select_date)

                WatchDateSelector(
                    watchDate = item.watchDate ?: item.addedAt,
                    onDateChanged = onDateChange
                )

                Title(R.string.note)

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
fun Title(@StringRes resId: Int) {
    Text(
        text = stringResource(resId),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun MediaInfoSection(item: MediaItem) {
    InfoRow(R.string.year, item.year?.toString())
    InfoRow(R.string.country, item.countries)
    InfoRow(R.string.director, item.director)
    InfoRow(R.string.duration, item.length)
    InfoRow(R.string.age, item.ageRating)
    InfoRow(R.string.actors, item.actors)
}

@Composable
fun GenreSection(item: MediaItem) {
    val genres = item.genres
    if (genres.isNullOrEmpty()) return
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item.genres.forEach { g -> GenreChip(g) }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun StatusSelector(currentStatus: MovieStatus?, onStatusClick: (MovieStatus) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MovieStatus.entries.forEach { status ->
            val icon = when (status) {
                MovieStatus.WANT_TO_WATCH -> Icons.Default.WatchLater
                MovieStatus.WATCHING -> Icons.Default.Visibility
                MovieStatus.WATCHED -> Icons.Default.CheckCircle
            }
            val labelResId = when (status) {
                MovieStatus.WANT_TO_WATCH -> R.string.want_to_watch
                MovieStatus.WATCHING -> R.string.watching
                MovieStatus.WATCHED -> R.string.watched
            }
            StatusTab(
                icon = icon,
                label = labelResId,
                selected = currentStatus == status,
                onClick = { onStatusClick(status) })
        }
    }
}

@Composable
fun StatusTab(
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
            tint = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(0.7f),
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
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

@Composable
fun InfoRow(@StringRes label: Int, value: String?) {
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
            stringResource(R.string.no_date)
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
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) { Text(stringResource(R.string.cancel)) }
            },
            content = {
                DatePicker(state = datePickerState)
            }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun PosterHeader(
    item: MediaItem
) {
    val context = LocalContext.current
    val request = remember(item.poster) {
        ImageRequest.Builder(context)
            .data(item.poster)
            .allowHardware(true)
            .placeholder(R.drawable.loading_img)
            .error(R.drawable.ic_connection_error)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }

    @Composable
    fun ratingColor(rating: Double): Color {
        return when {
            rating >= 8.0 -> gold
            rating >= 4.5 -> green
            else -> MaterialTheme.colorScheme.error
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            model = request,
            contentDescription = item.title,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
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
            text = item.title ?: stringResource(R.string.unknown_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        val ratingValue = item.rating
        val ratingText =
            ratingValue?.roundToOneSign()?.toString() ?: stringResource(R.string.unknown_value)

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = if (ratingValue != null) ratingColor(item.rating.roundToOneSign()) else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Text(
                text = ratingText,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DescriptionSection(
    item: MediaItem,
    initiallyExpanded: Boolean = false,
) {
    if (item.description.isNullOrBlank()) return

    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val maxLines = if (expanded) Int.MAX_VALUE else 4
    var isOverflowing by remember { mutableStateOf(false) }
    var checkNeeded by remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = item.description,
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