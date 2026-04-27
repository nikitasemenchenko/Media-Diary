package ru.magnum.mediadiary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskPerson(
    val name: String? = "",
    val enProfession: String? = ""
)