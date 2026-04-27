package ru.magnum.mediadiary.data.local.typeConverters

import androidx.room.TypeConverter
import ru.magnum.mediadiary.data.remote.dto.ContentType

class ContentTypeTypeConverter {

    @TypeConverter
    fun fromContentType(contentType: ContentType?): String? = contentType?.name

    @TypeConverter
    fun toContentType(value: String?): ContentType? {
        if (value.isNullOrBlank()) return null

        return runCatching {
            ContentType.valueOf(value)
        }.getOrNull() ?: ContentType.fromApiValue(value)
    }
}