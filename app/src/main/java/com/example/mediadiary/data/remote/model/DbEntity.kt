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
    val addedAt: Long = System.currentTimeMillis(),
    val watchStatus: String = "Хочу посмотреть",
    val userRating: Int? = null,
    val watchDate: Long? = null,
    val userNote: String? = null
){
    companion object {
//        fun fromSmallSearchResult(item: SearchResult): MediaItem{
//            return MediaItem(
//                id = item.id,
//                title = item.getItemTitle(),
//                year = item.getItemYear(),
//                description = null,
//                type = item.getItemType(),
//                rating = item.getItemRating(),
//                poster = item.getItemPoster(),
//                genres = item.getItemGenres(),
//                ageRating = null,
//                director = null,
//                countries = null,
//                actors = null,
//                length = null
//            )
//        }
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
    }
}