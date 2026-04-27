package ru.magnum.mediadiary.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.magnum.mediadiary.data.remote.dto.ContentType

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
    val type: ContentType? = null,
    val rating: Double? = 0.0,
    val poster: String? = "",
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

