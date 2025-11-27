package com.example.mediadiary.data.remote.model

import AppConstants
import androidx.annotation.StringRes
import com.example.mediadiary.R

enum class ContentType(
    val apiValue: String,
    @StringRes val resId: Int,
    @StringRes val pluralResId: Int
) {
    MOVIE(
        AppConstants.ApiConstants.MOVIE,
        R.string.movie,
        R.string.movies
    ),
    TV_SERIES(
        AppConstants.ApiConstants.TV_SERIES,
        R.string.series,
        R.string.series2
    ),
    CARTOON(
        AppConstants.ApiConstants.CARTOON,
        R.string.cartoon,
        R.string.cartoons
    ),
    ANIME(
        AppConstants.ApiConstants.ANIME,
        R.string.anime,
        R.string.anime
    ),
    ANIMATED_SERIES(
        AppConstants.ApiConstants.ANIMATED_SERIES,
        R.string.animated_series,
        R.string.animated_series2
    );

    companion object {
        fun fromApiValue(apiValue: String?): ContentType {
            if (apiValue == null) return MOVIE
            return entries.find { it.apiValue == apiValue } ?: MOVIE
        }

        fun fromName(name: String?): ContentType? {
            if (name == null) return MOVIE
            entries.find { it.name == name }?.let { return it }
            return fromApiValue(name)
        }
    }
}