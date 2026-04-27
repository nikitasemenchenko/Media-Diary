package ru.magnum.mediadiary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.magnum.mediadiary.data.local.typeConverters.ContentTypeTypeConverter
import ru.magnum.mediadiary.data.local.typeConverters.GenreListTypeConverter
import ru.magnum.mediadiary.data.local.typeConverters.MovieStatusTypeConverter

@Database(entities = [MediaItem::class], version = 1, exportSchema = true)
@TypeConverters(
    MovieStatusTypeConverter::class,
    GenreListTypeConverter::class,
    ContentTypeTypeConverter::class
)
abstract class MediaDiaryDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}