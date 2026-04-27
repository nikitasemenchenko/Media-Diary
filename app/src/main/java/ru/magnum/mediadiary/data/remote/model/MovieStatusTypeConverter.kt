package ru.magnum.mediadiary.data.remote.model

import androidx.room.TypeConverter

class MovieStatusTypeConverter {
    @TypeConverter
    fun toMovieStatus(value: String?): MovieStatus? {
        if (value.isNullOrBlank()) return null

        return runCatching {
            MovieStatus.valueOf(value)
        }.getOrNull()
    }

    @TypeConverter
    fun fromMovieStatus(status: MovieStatus?): String? {
        return status?.name
    }
}