package com.example.mediadiary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatusTypeConverter

@Database(entities = [MediaItem::class], version = 5, exportSchema = false)
@TypeConverters(MovieStatusTypeConverter::class)
abstract class MediaDiaryDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object {
        @Volatile
        private var Instance: MediaDiaryDatabase? = null
        fun getDatabase(context: Context): MediaDiaryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MediaDiaryDatabase::class.java, "mediadiary_database")
                    .fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}