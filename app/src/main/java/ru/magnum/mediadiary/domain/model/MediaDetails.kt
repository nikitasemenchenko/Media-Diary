package ru.magnum.mediadiary.domain.model

data class MediaDetails(
    val id: Int,
    val title: String?,
    val year: Int?,
    val description: String?,
    val type: MediaType?,
    val rating: Double?,
    val poster: String?,
    val genres: List<String>,
    val ageRating: String?,
    val director: String?,
    val actors: String?,
    val countries: String?,
    val length: String?,
    val watchStatus: WatchStatus?,
    val userRating: Int?,
    val watchDate: Long?,
    val addedAt: Long?,
    val userNote: String?
)