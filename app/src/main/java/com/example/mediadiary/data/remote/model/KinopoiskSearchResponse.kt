package com.example.mediadiary.data.remote.model

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
    val rating: KinopoiskRating?
){
    fun getItemTitle(): String{
        return name ?: alternativeName ?: "Без названия"
    }
    fun getItemYear(): String{
        return year?.toString() ?: "Неизвестно"
    }
    fun getItemPoster(): String {
        return poster?.url ?: ""
    }
    fun getItemGenres(): String {
        val res = mutableListOf<String>()
        for(i in genres){
            res.add(i.name)
        }
        return res.joinToString(", ")
    }

    fun isMovie(): Boolean = type == "movie"
    fun isTvSeries(): Boolean = type == "tv-series"
    fun isCartoon(): Boolean = type == "cartoon"
    fun isAnime(): Boolean = type == "anime"
    fun isAnimatedSeries(): Boolean = type == "animated-series"
    fun getItemType(): String {
        val whichType = ""
        return when(type) {
            "movie" -> "Фильм"
            "tv-series" -> "Сериал"
            "cartoon" -> "Мультфильм"
            "anime" -> "Аниме"
            else -> "Мультсериал"
        }
    }
    fun getItemRating(): Double {
        val kp = rating?.kp ?: 0.0
        val imdb = rating?.imdb ?: 0.0
        return if (kp > imdb) kp else imdb
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
){
    fun getItemTitle(): String{
        return name ?: alternativeName ?: "Без названия"
    }
    fun getItemYear(): String{
        return year?.toString() ?: "Неизвестно"
    }
    fun getItemPoster(): String {
        return poster?.url ?: ""
    }
    fun getItemGenres(): String {
        val res = mutableListOf<String>()
        for(i in genres){
            res.add(i.name)
        }
        return res.joinToString(", ")
    }
    fun getItemRating(): Double {
        val kp = rating?.kp ?: 0.0
        val imdb = rating?.imdb ?: 0.0
        return if (kp > imdb) kp else imdb
    }

    fun getAge(): String {
        return if(ageRating != null) "$ageRating+" else "-"
    }
    fun getCountriesList():String{
        if(countries == null) return "-"
        val countriesList = mutableListOf<String>()
        for(x in countries) countriesList.add(x.name)
        return countriesList.joinToString(", ")
    }

    fun isMovie(): Boolean = type == "movie"
    fun isTvSeries(): Boolean = type == "tv-series"
    fun isCartoon(): Boolean = type == "cartoon"
    fun isAnime(): Boolean = type == "anime"
    fun isAnimatedSeries(): Boolean = type == "animated-series"
    fun getItemType(): String {
        return when(type) {
            "movie" -> "Фильм"
            "tv-series" -> "Сериал"
            "cartoon" -> "Мультфильм"
            "anime" -> "Аниме"
            else -> "Мультсериал"
        }
    }

    fun getItemDescription():String{
        return description ?: "Нет описания"
    }
    fun getDuration(): String{
        return movieLength?.toString() ?: seriesLength?.toString() ?: "Неизвестно"
    }

    fun getDirectorName(): String {
        val director = persons?.firstOrNull {
           it.enProfession == "director"
        }
        return director?.name ?: "Неизвестно"
    }

    fun getActorsNames(): String {
        return persons
            ?.filter { it.enProfession == "actor" }
            ?.take(8)?.joinToString(", ") { it.name ?: "Неизвестно" }
            ?: "Нет информации об актерах"
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
    val name: String? = "Неизвестно",
    val enProfession: String
)