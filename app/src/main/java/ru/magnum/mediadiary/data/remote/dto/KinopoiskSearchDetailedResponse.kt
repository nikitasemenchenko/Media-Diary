package ru.magnum.mediadiary.data.remote.dto

import AppConstants
import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskSearchDetailedResponse(
    val id: Int,
    val name: String? = null,
    val alternativeName: String? = null,
    val enName: String? = null,
    val year: Int? = null,
    val description: String? = null,
    val type: String? = null,
    val rating: KinopoiskRating? = null,
    val poster: KinopoiskPoster? = null,
    val genres: List<KinopoiskGenres> = emptyList(),
    val names: List<KinopoiskName> = emptyList(),
    val countries: List<KinopoiskCountry>? = null,
    val movieLength: Int? = null,
    val seriesLength: Int? = null,
    val ageRating: Int? = null,
    val persons: List<KinopoiskPerson>? = null
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


    fun getItemRating(): Double? {
        val kp = rating?.kp
        val imdb = rating?.imdb
        return listOfNotNull(kp, imdb)
            .filter { it > 0.0 }
            .maxOrNull()
    }

    fun getAge(): String? {
        return ageRating?.let { "$it+" }
    }

    fun getCountriesList(): String? {
        return countries?.joinToString(", ") { it.name.toString() }
    }

    fun getItemType(): ContentType? {
        return ContentType.fromApiValue(type)
    }

    fun getDuration(): String? {
        return movieLength?.toString()
            ?: seriesLength?.toString()
    }

    fun getDirectorName(): String? {
        val director = persons?.firstOrNull {
            it.enProfession == AppConstants.ApiConstants.DIRECTOR
        }
        return director?.name
    }

    fun getActorsNames(): String? {
        return persons
            ?.filter { it.enProfession == AppConstants.ApiConstants.ACTOR }
            ?.take(AppConstants.Limits.ACTORS_LIMIT)
            ?.joinToString(", ") { it.name ?: "" }
            .let { if (it.isNullOrEmpty()) null else it }
    }
}