package ru.magnum.mediadiary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskName(
    val name: String? = null,
)