package ru.magnum.mediadiary.data.di

import AppConstants
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.magnum.mediadiary.data.local.MediaDao
import ru.magnum.mediadiary.data.local.MediaDiaryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MediaDiaryDatabase {
        return Room.databaseBuilder(
            context,
            MediaDiaryDatabase::class.java,
            AppConstants.DB_NAME
        ).build()
    }

    @Provides
    fun provideMediaDao(database: MediaDiaryDatabase): MediaDao {
        return database.mediaDao()
    }
}