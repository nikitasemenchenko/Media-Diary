package ru.magnum.mediadiary.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserWatchInfoSection(
    userRating: Int?,
    watchDate: Long?,
    userNote: String?,
    onRatingChange: (Int) -> Unit,
    onDateChange: (Long?) -> Unit,
    onNoteChange: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier
    ) {
        DetailsTitle(R.string.user_rating)

        StarRatingSelector(
            rating = userRating,
            onRatingChanged = onRatingChange
        )

        DetailsTitle(R.string.select_date)

        WatchDateSelector(
            watchDate = watchDate,
            onDateChanged = onDateChange
        )

        DetailsTitle(R.string.note)

        OutlinedTextField(
            value = userNote.orEmpty(),
            onValueChange = { value ->
                onNoteChange(value.ifBlank { null })
            },
            label = {
                Text(stringResource(R.string.note_label))
            },
            modifier = Modifier
                .padding(vertical = 4.dp),
            minLines = 2,
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
fun DetailsTitle(@StringRes resId: Int) {
    Text(
        text = stringResource(resId),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun StarRatingSelector(
    rating: Int?,
    onRatingChanged: (Int) -> Unit
) {
    var selectedRating by remember(rating) {
        mutableIntStateOf(rating ?: 0)
    }

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        for (i in 1..10) {
            Icon(
                imageVector = if (i <= selectedRating) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.Star
                },
                contentDescription = i.toString(),
                tint = if (i <= selectedRating) {
                    Color.Yellow
                } else {
                    Color.DarkGray
                },
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {
                        selectedRating = i
                        onRatingChanged(i)
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchDateSelector(
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
                onClick = { showDialog = true }
            ) {
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
                TextButton(
                    onClick = {
                        onDateChanged(datePickerState.selectedDateMillis)
                        showDialog = false
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            content = {
                DatePicker(state = datePickerState)
            }
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}