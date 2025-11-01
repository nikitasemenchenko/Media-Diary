package com.example.mediadiary.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mediadiary.data.remote.model.MediaItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MediaItem)

    @Update
    suspend fun update(item: MediaItem)

    @Delete
    suspend fun delete(item: MediaItem)

    @Query("SELECT * from media_items WHERE id = :id")
    suspend fun findById(id: Int): MediaItem?

    @Query("SELECT * from media_items ORDER BY addedAt DESC")
    fun getAllItems(): Flow<List<MediaItem>>

    @Query("SELECT COUNT(*) FROM media_items")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM media_items WHERE watchStatus = 'Просмотрено'")
    suspend fun getWatchedCount(): Int

    @Query("SELECT COUNT(*) FROM media_items WHERE watchStatus = 'Смотрю'")
    suspend fun getWatchingCount(): Int

    @Query("SELECT COUNT(*) FROM media_items WHERE watchStatus = 'Хочу посмотреть'")
    suspend fun getWantToWatchCount(): Int

}