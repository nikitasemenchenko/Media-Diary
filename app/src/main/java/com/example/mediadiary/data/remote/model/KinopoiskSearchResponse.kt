package com.example.mediadiary.data.remote.model

import AppConstants
import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskSearchResponse(
    val docs: List<SearchResult>

)

@Serializable
data class SearchResult(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val year: Int?,
    val type: String,
    val poster: KinopoiskPoster? = null,
    val genres: List<KinopoiskGenres> = emptyList(),
    var rating: KinopoiskRating?
) {
    fun getItemTitle(): String? {
        return name ?: alternativeName
    }

    fun getItemGenres(): List<String> {
        return genres.map { it.name }.take(AppConstants.Limits.GENRES_LIMIT)
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

@Serializable
data class KinopoiskSearchDetailedResponse(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val year: Int?,
    val description: String?,
    val type: String,
    val rating: KinopoiskRating? = null,
    val poster: KinopoiskPoster? = null,
    val genres: List<KinopoiskGenres> = emptyList(),
    val countries: List<KinopoiskCountry>?,
    val movieLength: Int?,
    val seriesLength: Int?,
    val ageRating: Int?,
    val persons: List<KinopoiskPerson>?
) {
    fun getItemTitle(): String? {
        return name ?: alternativeName
    }
    fun getItemGenres(): List<String> {
        return genres.map { it.name }.take(AppConstants.Limits.GENRES_LIMIT)
    }


    fun getItemRating(): Double? {
        val kp = rating?.kp
        val imdb = rating?.imdb
        val availableRatings = listOfNotNull(kp, imdb)
        return availableRatings
            .maxOrNull()
    }

    fun getAge(): String? {
        return ageRating?.let { "$it+" }
    }

    fun getCountriesList(): String? {
        return countries?.joinToString(", ") { it.name }
    }

    fun getItemType(): ContentType {
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

@Serializable
data class KinopoiskRating(
    val kp: Double,
    val imdb: Double,
)

@Serializable
data class KinopoiskPoster(
    val url: String?,
)

@Serializable
data class KinopoiskGenres(
    val name: String,
)

@Serializable
data class KinopoiskCountry(
    val name: String
)

@Serializable
data class KinopoiskPerson(
    val name: String?,
    val enProfession: String
)