package com.example.mediadiary.data.remote.model

import androidx.room.TypeConverter

class MovieStatusTypeConverter {
    @TypeConverter
    fun toMovieStatus(value: String?): MovieStatus? {
        return value?.let { MovieStatus.valueOf(it) }
    }

    @TypeConverter
    fun fromMovieStatus(status: MovieStatus?): String? {
        return status?.name
    }
}