package com.example.mediadiary.data.remote.model

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.mediadiary.R

@Entity(
    "media_items",
    indices = [
        Index(value = ["watchStatus", "addedAt"]),
        Index(value = ["type", "addedAt"]),
        Index(value = ["id", "watchStatus"])
    ]
)
data class MediaItem(
    @PrimaryKey val id: Int,
    val title: String?,
    val year: Int?,
    val description: String? = null,
    val type: ContentType?,
    val rating: Double?,
    val poster: String?,
    val genres: List<String>? = emptyList(),
    val ageRating: String? = null,
    val director: String? = null,
    val actors: String? = null,
    val countries: String? = null,
    val length: String? = null,
    val watchStatus: MovieStatus? = null,
    val userRating: Int? = null,
    val watchDate: Long? = null,
    val addedAt: Long? = System.currentTimeMillis(),
    val userNote: String? = null
)

enum class MovieStatus(@StringRes val resId: Int) {
    WANT_TO_WATCH(R.string.want_to_watch),
    WATCHING(R.string.watching),
    WATCHED(R.string.watched)
}

