package ru.magnum.mediadiary.domain.model

data class MediaPreview(
    val id: Int,
    val title: String?,
    val year: Int?,
    val type: MediaType?,
    val rating: Double?,
    val poster: String?,
    val genres: List<String>
)