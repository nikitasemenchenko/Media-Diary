package ru.magnum.mediadiary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskRating(
    val kp: Double? = 0.0,
    val imdb: Double? = 0.0,
)