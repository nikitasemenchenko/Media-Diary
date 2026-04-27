package ru.magnum.mediadiary.data.remote.dto

import AppConstants
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val id: Int,
    val name: String? = null,
    val alternativeName: String? = "",
    val year: Int? = 0,
    val type: String? = "",
    val poster: KinopoiskPoster? = null,
    val genres: List<KinopoiskGenres> = emptyList(),
    var rating: KinopoiskRating? = null
) {
    fun getItemTitle(): String? {
        return name ?: alternativeName
    }

    fun getItemGenres(): List<String> {
        return genres.map { it.name ?: ""}.take(AppConstants.Limits.GENRES_LIMIT)
    }

    fun getItemType(): ContentType {
        return ContentType.fromApiValue(type)
    }

    fun getItemRating(): Double? {
        val kp = rating?.kp
        val imdb = rating?.imdb
        val availableRatings = listOfNotNull(kp, imdb)
        return availableRatings
            .maxOrNull()
    }
}