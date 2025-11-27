package com.example.mediadiary.data.remote.model

import androidx.room.TypeConverter

class ContentTypeTypeConverter {

    @TypeConverter
    fun fromContentType(contentType: ContentType?): String? = contentType?.name

    @TypeConverter
    fun toContentType(value: String?): ContentType? = ContentType.fromName(value)
}