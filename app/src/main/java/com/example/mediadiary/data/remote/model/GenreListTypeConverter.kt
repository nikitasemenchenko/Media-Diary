package com.example.mediadiary.data.remote.model

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GenreListTypeConverter {
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(genres: List<String>?): String? {
        if (genres == null) return null
        return json.encodeToString(genres)
    }

    @TypeConverter
    fun toList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return json.decodeFromString<List<String>>(value)
    }

}