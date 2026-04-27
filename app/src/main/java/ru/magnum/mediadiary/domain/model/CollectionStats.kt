package ru.magnum.mediadiary.domain.model

data class CollectionStats(
    val total: Int,
    val watched: Int,
    val watching: Int,
    val wantToWatch: Int
)