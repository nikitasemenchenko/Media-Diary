package ru.magnum.mediadiary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskPoster(
    val url: String? = "",
)