package ru.magnum.mediadiary.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.presentation.components.DescriptionSection
import ru.magnum.mediadiary.presentation.components.DetailsGenreSection
import ru.magnum.mediadiary.presentation.components.DetailsInfoSection
import ru.magnum.mediadiary.presentation.components.DetailsPosterImage
import ru.magnum.mediadiary.presentation.components.DetailsTitle
import ru.magnum.mediadiary.presentation.components.UserWatchInfoSection
import ru.magnum.mediadiary.presentation.components.WatchStatusSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    item: MediaDetails,
    onStatusClick: (WatchStatus) -> Unit,
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
            DetailsPosterImage(item)

            DetailsInfoSection(item)

            DetailsGenreSection(item.genres)

            DescriptionSection(item.description)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
            )

            DetailsTitle(R.string.status)

            WatchStatusSelector(
                currentStatus = item.watchStatus,
                onStatusClick = onStatusClick
            )

            if (item.watchStatus == WatchStatus.WATCHED) {
                UserWatchInfoSection(
                    userRating = item.userRating,
                    watchDate = item.watchDate ?: item.addedAt,
                    userNote = item.userNote,
                    onRatingChange = onRatingChange,
                    onDateChange = onDateChange,
                    onNoteChange = onNoteChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}