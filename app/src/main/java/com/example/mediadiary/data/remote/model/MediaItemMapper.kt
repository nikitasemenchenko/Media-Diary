package com.example.mediadiary.data.remote.model

class MediaItemMapper {

    fun fromDetailedSearchResult(item: KinopoiskSearchDetailedResponse): MediaItem {
        return MediaItem(
            id = item.id,
            title = item.getItemTitle(),
            year = item.year,
            description = item.description,
            type = item.getItemType(),
            rating = item.getItemRating(),
            poster = item.poster?.url,
            genres = item.getItemGenres(),
            ageRating = item.getAge(),
            director = item.getDirectorName(),
            actors = item.getActorsNames(),
            countries = item.getCountriesList(),
            length = item.getDuration()

        )
    }

    fun toWishListItem(item: KinopoiskSearchDetailedResponse): MediaItem {
        return fromDetailedSearchResult(item).copy(
            watchStatus = MovieStatus.WANT_TO_WATCH,
            addedAt = System.currentTimeMillis()
        )
    }

}