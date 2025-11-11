package com.example.mediadiary.data.remote.model

import androidx.room.TypeConverter

class MovieStatusTypeConverter {
    @TypeConverter
    fun toMovieStatus(value: String?): MovieStatus? {
        return MovieStatus.fromString(value ?: return null)
    }

    @TypeConverter
    fun fromMovieStatus(status: MovieStatus?): String? {
        return status?.statusName
    }
}