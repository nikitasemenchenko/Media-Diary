package ru.magnum.mediadiary.data.mappers

import ru.magnum.mediadiary.data.local.MediaItem
import ru.magnum.mediadiary.data.local.MediaStats
import ru.magnum.mediadiary.data.local.MovieStatus
import ru.magnum.mediadiary.data.remote.dto.ContentType
import ru.magnum.mediadiary.data.remote.dto.KinopoiskSearchDetailedResponse
import ru.magnum.mediadiary.data.remote.dto.SearchResult
import ru.magnum.mediadiary.domain.model.CollectionStats
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaPreview
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import javax.inject.Inject

class MediaMapper @Inject constructor() {

    fun watchStatusToData(status: WatchStatus?): MovieStatus? {
        return when (status) {
            WatchStatus.WANT_TO_WATCH -> MovieStatus.WANT_TO_WATCH
            WatchStatus.WATCHING -> MovieStatus.WATCHING
            WatchStatus.WATCHED -> MovieStatus.WATCHED
            else -> null
        }
    }

    fun movieStatusToDomain(status: MovieStatus?): WatchStatus? {
        return when (status) {
            MovieStatus.WANT_TO_WATCH -> WatchStatus.WANT_TO_WATCH
            MovieStatus.WATCHING -> WatchStatus.WATCHING
            MovieStatus.WATCHED -> WatchStatus.WATCHED
            else -> null
        }
    }

    fun contentTypeToDomain(type: ContentType?): MediaType? {
        return when (type) {
            ContentType.MOVIE -> MediaType.MOVIE
            ContentType.TV_SERIES -> MediaType.TV_SERIES
            ContentType.CARTOON -> MediaType.CARTOON
            ContentType.ANIME -> MediaType.ANIME
            ContentType.ANIMATED_SERIES -> MediaType.ANIMATED_SERIES
            else -> null
        }
    }

    fun mediaTypeToData(type: MediaType?): ContentType? {
        return when (type) {
            MediaType.MOVIE -> ContentType.MOVIE
            MediaType.TV_SERIES -> ContentType.TV_SERIES
            MediaType.CARTOON -> ContentType.CARTOON
            MediaType.ANIME -> ContentType.ANIME
            MediaType.ANIMATED_SERIES -> ContentType.ANIMATED_SERIES
            else -> null
        }
    }

    fun searchResultToPreview(item: SearchResult): MediaPreview {
        return MediaPreview(
            id = item.id,
            title = item.getItemTitle(),
            year = item.year,
            type = contentTypeToDomain(item.getItemType()),
            rating = item.getItemRating(),
            poster = item.getPosterUrl(),
            genres = item.getItemGenres()
        )
    }

    fun detailedResponseToDetails(item: KinopoiskSearchDetailedResponse): MediaDetails {
        return MediaDetails(
            id = item.id,
            title = item.getItemTitle(),
            year = item.year,
            description = item.description,
            type = contentTypeToDomain(item.getItemType()),
            rating = item.getItemRating(),
            poster = item.getPosterUrl(),
            genres = item.getItemGenres(),
            ageRating = item.getAge(),
            director = item.getDirectorName(),
            actors = item.getActorsNames(),
            countries = item.getCountriesList(),
            length = item.getDuration(),
            watchStatus = null,
            userRating = null,
            watchDate = null,
            addedAt = null,
            userNote = null
        )
    }

    fun detailedResponseToWishlistEntity(item: KinopoiskSearchDetailedResponse): MediaItem {
        return MediaItem(
            id = item.id,
            title = item.getItemTitle(),
            year = item.year,
            description = item.description,
            type = item.getItemType(),
            rating = item.getItemRating(),
            poster = item.getPosterUrl(),
            genres = item.getItemGenres(),
            ageRating = item.getAge(),
            director = item.getDirectorName(),
            actors = item.getActorsNames(),
            countries = item.getCountriesList(),
            length = item.getDuration(),
            watchStatus = MovieStatus.WANT_TO_WATCH,
            userRating = null,
            watchDate = null,
            addedAt = System.currentTimeMillis(),
            userNote = null
        )
    }

    fun entityToDetails(item: MediaItem): MediaDetails {
        return MediaDetails(
            id = item.id,
            title = item.title,
            year = item.year,
            description = item.description,
            type = contentTypeToDomain(item.type),
            rating = item.rating,
            poster = item.poster,
            genres = item.genres.orEmpty(),
            ageRating = item.ageRating,
            director = item.director,
            actors = item.actors,
            countries = item.countries,
            length = item.length,
            watchStatus = movieStatusToDomain(item.watchStatus),
            userRating = item.userRating,
            watchDate = item.watchDate,
            addedAt = item.addedAt,
            userNote = item.userNote
        )
    }

    fun detailsToEntity(item: MediaDetails): MediaItem {
        return MediaItem(
            id = item.id,
            title = item.title,
            year = item.year,
            description = item.description,
            type = mediaTypeToData(item.type),
            rating = item.rating,
            poster = item.poster,
            genres = item.genres,
            ageRating = item.ageRating,
            director = item.director,
            actors = item.actors,
            countries = item.countries,
            length = item.length,
            watchStatus = watchStatusToData(item.watchStatus),
            userRating = item.userRating,
            watchDate = item.watchDate,
            addedAt = item.addedAt ?: System.currentTimeMillis(),
            userNote = item.userNote
        )
    }

    fun statsToDomain(stats: MediaStats): CollectionStats {
        return CollectionStats(
            total = stats.total,
            watched = stats.watched,
            watching = stats.watching,
            wantToWatch = stats.wantToWatch
        )
    }
}