package com.example.mediadiary.data

object AppConstants {
    const val BASE_URL = "https://api.kinopoisk.dev/v1.4/"
    const val TIMEOUT_MILLIS = 5_000L
    const val SEARCH_DELAY = 1000L

    val already_in_collection = "Уже в коллекции"
    val successfully_added = "Добавлено в \"Хочу посмотреть\""
    val adding_error = "Ошибка"
    val loading_error = "Не удалось загрузить фильм"
}

fun Double.roundToOneSign(): Double = kotlin.math.round(this * 10) / 10