package com.example.mediadiary.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("media_items")
data class MediaItem(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val description: String?,
    val type: String,
    val rating: Double,
    val poster: String,
    val genres: String,
    val ageRating: String?,
    val director: String?,
    val actors: String?,
    val countries: String?,
    val length: String?,
    val watchStatus: MovieStatus? = null,
    val userRating: Int? = null,
    val watchDate: Long? = null,
    val addedAt: Long? = System.currentTimeMillis(),
    val userNote: String? = null
){
    fun isMovie(): Boolean = type == "Фильм"
    fun isTvSeries(): Boolean = type == "Сериал"
    fun isCartoon(): Boolean = type == "Мультфильм"
    fun isAnime(): Boolean = type == "Аниме"
    fun isAnimatedSeries(): Boolean = type == "Мультсериал"
    companion object {
        fun fromDetailedSearchResult(item: KinopoiskSearchDetailedResponse): MediaItem{
            return MediaItem(
                id = item.id,
                title = item.getItemTitle(),
                year = item.getItemYear(),
                description = item.getItemDescription(),
                type = item.getItemType(),
                rating = item.getItemRating(),
                poster = item.getItemPoster(),
                genres = item.getItemGenres(),
                ageRating = item.getAge(),
                director = item.getDirectorName(),
                actors = item.getActorsNames(),
                countries = item.getCountriesList(),
                length = item.getDuration()

            )
        }
        fun onAddToWishList(item: KinopoiskSearchDetailedResponse): MediaItem{
            return MediaItem(
                id = item.id,
                title = item.getItemTitle(),
                year = item.getItemYear(),
                description = item.getItemDescription(),
                type = item.getItemType(),
                rating = item.getItemRating(),
                poster = item.getItemPoster(),
                genres = item.getItemGenres(),
                ageRating = item.getAge(),
                director = item.getDirectorName(),
                actors = item.getActorsNames(),
                countries = item.getCountriesList(),
                length = item.getDuration(),
                watchStatus = MovieStatus.WANT_TO_WATCH

            )
        }
    }
}

enum class MovieStatus(val statusName: String) {
    WATCHED("Просмотрено"),
    WATCHING("Смотрю"),
    WANT_TO_WATCH("Хочу посмотреть");
    companion object {
        fun fromString(value: String): MovieStatus? {
            return entries.find { it.statusName == value }
        }
    }
}
