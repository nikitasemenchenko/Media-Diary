package ru.magnum.mediadiary.data.remote.dto

import AppConstants
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val id: Int,
    val name: String? = null,
    val alternativeName: String? = null,
    val enName: String? = null,
    val year: Int? = null,
    val type: String? = null,
    val poster: KinopoiskPoster? = null,
    val genres: List<KinopoiskGenres> = emptyList(),
    val names: List<KinopoiskName> = emptyList(),
    var rating: KinopoiskRating? = null
) {
    fun getItemTitle(): String? {
        return name?.takeIf { it.isNotBlank() }
            ?: alternativeName?.takeIf { it.isNotBlank() }
            ?: enName?.takeIf { it.isNotBlank() }
            ?: names.firstNotNullOfOrNull { item ->
                item.name?.takeIf { it.isNotBlank() }
            }
    }

    fun getPosterUrl(): String? {
        return poster?.url?.takeIf { it.isNotBlank() }
            ?: poster?.previewUrl?.takeIf { it.isNotBlank() }
    }

    fun getItemGenres(): List<String> {
        return genres
            .mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } }
            .take(AppConstants.Limits.GENRES_LIMIT)
    }

    fun getItemType(): ContentType? {
        return ContentType.fromApiValue(type)
    }

    fun getItemRating(): Double? {
        val kp = rating?.kp
        val imdb = rating?.imdb
        return listOfNotNull(kp, imdb)
            .filter { it > 0.0 }
            .maxOrNull()
    }
}