package ru.magnum.mediadiary.data.local.typeConverters

import androidx.room.TypeConverter
import ru.magnum.mediadiary.data.local.MovieStatus

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