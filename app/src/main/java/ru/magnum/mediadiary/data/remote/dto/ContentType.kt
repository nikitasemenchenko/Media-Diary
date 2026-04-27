package ru.magnum.mediadiary.data.remote.dto

import AppConstants

enum class ContentType(
    val apiValue: String
) {
    MOVIE(AppConstants.ApiConstants.MOVIE),
    TV_SERIES(AppConstants.ApiConstants.TV_SERIES),
    CARTOON(AppConstants.ApiConstants.CARTOON),
    ANIME(AppConstants.ApiConstants.ANIME),
    ANIMATED_SERIES(AppConstants.ApiConstants.ANIMATED_SERIES);

    companion object {
        fun fromApiValue(apiValue: String?): ContentType {
            if (apiValue == null) return MOVIE
            return entries.find { it.apiValue == apiValue } ?: MOVIE
        }

        fun fromName(name: String?): ContentType {
            if (name == null) return MOVIE
            entries.find { it.name == name }?.let { return it }
            return fromApiValue(name)
        }
    }
}