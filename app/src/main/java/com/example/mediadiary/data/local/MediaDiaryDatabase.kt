package com.example.mediadiary.data.local

import com.example.mediadiary.data.remote.model.MediaItem
import kotlin.jvm.java
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MediaItem::class], version = 1, exportSchema = false)
abstract class MediaDiaryDatabase: RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object{
        @Volatile
        private var Instance: MediaDiaryDatabase? = null
        fun getDatabase(context: Context): MediaDiaryDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MediaDiaryDatabase::class.java, "mediadiary_database").fallbackToDestructiveMigration().build().also { Instance = it}
            }
        }
    }
}