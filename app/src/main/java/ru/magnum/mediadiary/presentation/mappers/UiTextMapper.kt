package ru.magnum.mediadiary.presentation.mappers

import androidx.annotation.StringRes
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus

@StringRes
fun WatchStatus.titleRes(): Int {
    return when (this) {
        WatchStatus.WANT_TO_WATCH -> R.string.want_to_watch
        WatchStatus.WATCHING -> R.string.watching
        WatchStatus.WATCHED -> R.string.watched
    }
}

@StringRes
fun MediaType.titleRes(): Int {
    return when (this) {
        MediaType.MOVIE -> R.string.movie
        MediaType.TV_SERIES -> R.string.series
        MediaType.CARTOON -> R.string.cartoon
        MediaType.ANIME -> R.string.anime
        MediaType.ANIMATED_SERIES -> R.string.animated_series
    }
}

@StringRes
fun MediaType.pluralTitleRes(): Int {
    return when (this) {
        MediaType.MOVIE -> R.string.movies
        MediaType.TV_SERIES -> R.string.series2
        MediaType.CARTOON -> R.string.cartoons
        MediaType.ANIME -> R.string.anime
        MediaType.ANIMATED_SERIES -> R.string.animated_series2
    }
}