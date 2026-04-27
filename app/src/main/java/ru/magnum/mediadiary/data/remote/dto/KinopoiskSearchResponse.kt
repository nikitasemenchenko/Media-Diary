package ru.magnum.mediadiary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class KinopoiskSearchResponse(
    val docs: List<SearchResult>

)