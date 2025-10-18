package com.example.mediadiary.data.remote.model

import android.R.attr.name
import android.R.attr.rating
import android.R.attr.type
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
    val description: String?,
    val type: String,
    val rating: KinopoiskRating? = null,
    val poster: KinopoiskPoster? = null,
    val genres: List<KinopoiskGenres> = emptyList()
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
    fun getItemRating(): String {
        val kp = rating?.kp ?: 0.0
        val imdb = rating?.imdb ?: 0.0
        return if (kp > imdb) kp.toString() else imdb.toString()
    }

    fun isMovie(): Boolean = type == "movie"
    fun isTvSeries(): Boolean = type == "tv-series"
    fun isCartoon(): Boolean = type == "cartoon"
    fun isAnime(): Boolean = type == "anime"
    fun isAnimatedSeries(): Boolean = type == "animated-series"
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