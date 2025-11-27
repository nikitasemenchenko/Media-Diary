package com.example.mediadiary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mediadiary.data.remote.model.ContentTypeTypeConverter
import com.example.mediadiary.data.remote.model.GenreListTypeConverter
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatusTypeConverter

@Database(entities = [MediaItem::class], version = 8, exportSchema = false)
@TypeConverters(
    MovieStatusTypeConverter::class,
    GenreListTypeConverter::class,
    ContentTypeTypeConverter::class
)
abstract class MediaDiaryDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object {
        @Volatile
        private var INSTANCE: MediaDiaryDatabase? = null
        private const val DB_NAME = "mediadiary_database"
        fun getDatabase(context: Context): MediaDiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(appContext: Context): MediaDiaryDatabase {
            return Room.databaseBuilder(
                appContext,
                MediaDiaryDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}